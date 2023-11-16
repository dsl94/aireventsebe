package com.airevents.service;

import com.airevents.dto.response.StravaActivityResponse;
import com.airevents.dto.response.StravaLoginResponse;
import com.airevents.dto.response.StravaResponse;
import com.airevents.entity.Challenge;
import com.airevents.entity.User;
import com.airevents.entity.UserChallenge;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.ChallengeRepository;
import com.airevents.repository.UserChallengeRepository;
import com.airevents.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Component
public class StravaSync {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void trackOverduePayments() {
        System.out.println("Syncing strava!!!!");
        List<Challenge> challenges = challengeRepository.findAllByOrderByStartDateDesc();
        for (Challenge challenge : challenges) {
            if (LocalDateTime.now().plusDays(2).isBefore(challenge.getEndDate())) {
                Set<UserChallenge> users = challenge.getUserChallenges();
                for (UserChallenge uc : users) {
                    try {
                        double distance = sync(uc.getUser(), challenge.getStartDate(), challenge.getEndDate());
                        uc.setDistance(distance);
                        userChallengeRepository.saveAndFlush(uc);
                    } catch (IOException e) {

                    }
                }
                challenge.setLastSync(LocalDateTime.now());
                challengeRepository.save(challenge);
            }
        }
    }

    private Double sync(User user, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        String activitiesUrl = "https://www.strava.com/api/v3/athlete/activities?before=" + endDate.atZone(ZoneId.systemDefault()).toEpochSecond() + "&after=" + startDate.atZone(ZoneId.systemDefault()).toEpochSecond() + "&per_page=200";
        String token = login(user.getStravaRefreshToken());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        double distance = 0;
        try {

            HttpGet request = new HttpGet(activitiesUrl);
            request.setHeader("Authorization", "Bearer " + token);
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
                    List<StravaActivityResponse> activities = objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, StravaActivityResponse.class));
                    for (StravaActivityResponse activity : activities) {
                        if (activity.getSport_type().equals("Run")) {
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

        return distance/1000;
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
        if (token.isEmpty()) {
            throw new RcnException(ErrorCode.NOT_FOUND,"Greska sa prijavom");
        }

        return token;
    }
}
