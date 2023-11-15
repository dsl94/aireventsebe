package com.airevents.service;

import com.airevents.dto.mapper.ChallengeMapper;
import com.airevents.dto.mapper.RaceMapper;
import com.airevents.dto.request.ChallengeRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.ChallengeResponse;
import com.airevents.dto.response.RaceResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ChallengeResponse retractFrom(String username, Long raceId) {
        Challenge challenge = challengeRepository.findById(raceId)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Izazov nije pronadjen"));

        User user = userRepository.findByUsernameIgnoreCase(username);

        UserChallenge uc = userChallengeRepository.findByChallengeAndUser(challenge, user)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Greska"));

        userChallengeRepository.delete(uc);

        return ChallengeMapper.entityToResponse(challenge);
    }
}
