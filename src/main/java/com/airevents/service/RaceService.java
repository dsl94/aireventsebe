package com.airevents.service;

import com.airevents.dto.mapper.RaceMapper;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.RaceResponse;
import com.airevents.entity.Race;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceService {

    @Autowired
    private RaceRepository raceRepository;

    public List<RaceResponse> getAllRaces() {
        return raceRepository.findAllByOrderByDateOfRaceDesc()
                .stream()
                .map(RaceMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public RaceResponse getRace(Long id) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Trka nije pronadjena"));

        return RaceMapper.entityToResponse(race);
    }

    public void createRace(RaceRequest request) {
        Race race = RaceMapper.requestToEntity(request);

        raceRepository.save(race);
    }

    public RaceResponse updateRace(Long id, RaceRequest request) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Trka nije pronadjena"));

        Race updated = RaceMapper.requestToEntity(request);
        race.setDateOfRace(updated.getDateOfRace());
        race.setRaceTitle(updated.getRaceTitle());
        race.setDistances(updated.getDistances());

        return RaceMapper.entityToResponse(race);
    }

    public void deleteRace(Long id) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Trka nije pronadjena"));

        raceRepository.delete(race);
    }
}
