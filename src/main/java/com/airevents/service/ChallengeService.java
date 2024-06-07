package com.airevents.service;

import com.airevents.dto.mapper.ChallengeMapper;
import com.airevents.dto.mapper.RaceMapper;
import com.airevents.dto.request.ChallengeRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.ChallengeResponse;
import com.airevents.dto.response.RaceResponse;
import com.airevents.dto.response.StravaActivityResponse;
import com.airevents.dto.response.StravaLoginResponse;
import com.airevents.entity.Challenge;
import com.airevents.entity.Race;
import com.airevents.entity.User;
import com.airevents.entity.UserChallenge;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.ChallengeRepository;
import com.airevents.repository.RaceRepository;
import com.airevents.repository.UserChallengeRepository;
import com.airevents.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserChallengeRepository userChallengeRepository;

    public List<ChallengeResponse> getAll() {
        return challengeRepository.findAllByOrderByStartDateDesc()
                .stream()
                .map(ChallengeMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ChallengeResponse getOne(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        return ChallengeMapper.entityToResponse(challenge);
    }

    public void create(ChallengeRequest request) {
        Challenge challenge = ChallengeMapper.requestToEntity(request);

        challengeRepository.save(challenge);
    }

    public ChallengeResponse update(Long id, ChallengeRequest request) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        Challenge updated = ChallengeMapper.requestToEntity(request);
        challenge.setStartDate(updated.getStartDate());
        challenge.setEndDate(updated.getEndDate());
        challenge.setTitle(updated.getTitle());

        challengeRepository.save(challenge);

        return ChallengeMapper.entityToResponse(challenge);
    }

    public void delete(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        challengeRepository.delete(challenge);
    }

    public ChallengeResponse goTo(String username, Long raceId) {
        Challenge challenge = challengeRepository.findById(raceId)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        User user = userRepository.findByUsernameIgnoreCase(username);

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setChallenge(challenge);
        userChallenge.setUser(user);
        userChallengeRepository.saveAndFlush(userChallenge);

        userRepository.saveAndFlush(user);

        return ChallengeMapper.entityToResponse(challenge);
    }

    public ChallengeResponse retractFrom(String username, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        User user = userRepository.findByUsernameIgnoreCase(username);

        UserChallenge uc = userChallengeRepository.findByChallengeAndUser(challenge, user)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Greska"));

        userChallengeRepository.deleteById(uc.getId());

        return ChallengeMapper.entityToResponse(challenge);
    }

    public void syncChallenge(Long id) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));
        if (LocalDateTime.now().plusDays(2).isBefore(challenge.getEndDate())) {
            Set<UserChallenge> users = challenge.getUserChallenges();
            for (UserChallenge uc : users) {
                try {
                    CalculationResponse response = sync(uc.getUser(), challenge.getStartDate(), challenge.getEndDate());
                    if (response == null) {
                        System.out.println("Problem with sync for user: " + uc.getUser().getUsername());
                    } else {
                        uc.setDistance(response.getTotalDistance());
                        uc.setPerMonth(ow.writeValueAsString(response.getByMonth()));
                        userChallengeRepository.saveAndFlush(uc);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            challenge.setLastSync(LocalDateTime.now());
            challengeRepository.save(challenge);
        }
    }

    private CalculationResponse sync(User user, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        String activitiesUrl = "https://www.strava.com/api/v3/athlete/activities?before=" + endDate.atZone(ZoneId.systemDefault()).toEpochSecond() + "&after=" + startDate.atZone(ZoneId.systemDefault()).toEpochSecond() + "&per_page=200";
        String token = login(user.getStravaRefreshToken());
        if (token == null) {
            return null;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<YearMonth> monthsBetween = getMonthsBetween(startDate, endDate);
        double distance = 0;
        Map<String, Double> distancePerMonth = new HashMap<>();

        for (YearMonth month : monthsBetween) {
            distancePerMonth.put(month.getMonth().name(), 0.0); // Initialize distances for each month as 0.0
        }
        try {

            HttpGet request = new HttpGet(activitiesUrl);
            request.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                System.out.println(" Sync Result for user: " + user.getFullName());

                // Get HttpResponse Status
                System.out.println(response.getProtocolVersion());              // HTTP/1.1
                System.out.println(response.getStatusLine().getStatusCode());   // 200
                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    List<StravaActivityResponse> activities = objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, StravaActivityResponse.class));
                    for (StravaActivityResponse activity : activities) {
                        if (activity.getSport_type().equals("Run") || activity.getSport_type().equals("VirtualRun")
                                || activity.getSport_type().equals("TrailRun")) {
                            LocalDateTime activityDate = activity.getStart_date_local(); // Assuming StravaActivityResponse has a method to get activity date
                            YearMonth activityYearMonth = YearMonth.from(activityDate);
                            String monthName = activityYearMonth.getMonth().name();
                            double distanceOfMonth = distancePerMonth.getOrDefault(monthName, 0.0);
                            distanceOfMonth += activity.getDistance();
                            distancePerMonth.put(monthName, distanceOfMonth);
                            distance += activity.getDistance();
                        }
                    }
                }

            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }

        double total =  distance/1000;
        return new CalculationResponse(total, distancePerMonth);
    }

    private String login(String refreshToken) throws IOException {
        String url = "https://www.strava.com/api/v3/oauth/token?client_id=116659&client_secret=f2699193cc92b28b75a0db52fcb522704f637121&grant_type=refresh_token&refresh_token=" + refreshToken;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String token = "";
        try {

            HttpPost request = new HttpPost(url);

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                // Get HttpResponse Status
                System.out.println(response.getProtocolVersion());              // HTTP/1.1
                System.out.println(response.getStatusLine().getStatusCode());   // 200
                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    StravaLoginResponse loginResponse = objectMapper.readValue(result, StravaLoginResponse.class);
                    token = loginResponse.getAccess_token();
                }

            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
        if (token == null || token.isEmpty()) {
            return null;
        }

        return token;
    }

    public List<YearMonth> getMonthsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<YearMonth> monthsList = new ArrayList<>();

        YearMonth startYearMonth = YearMonth.from(startDate);
        YearMonth endYearMonth = YearMonth.from(endDate);

        YearMonth currentYearMonth = startYearMonth;

        while (!currentYearMonth.isAfter(endYearMonth)) {
            monthsList.add(currentYearMonth);
            currentYearMonth = currentYearMonth.plusMonths(1);
        }

        return monthsList;
    }
}
