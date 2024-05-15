package com.airevents.service;

import com.airevents.dto.mapper.RaceTypeMapper;
import com.airevents.dto.response.RaceTypeWithDistancesResponse;
import com.airevents.repository.RaceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceTypeService {
    @Autowired
    private RaceTypeRepository raceTypeRepository;

    public List<RaceTypeWithDistancesResponse> getAll() {
        return raceTypeRepository
                .findAll()
                .stream()
                .map(RaceTypeMapper::mapToRaceTypeResponse)
                .collect(Collectors.toList());
    }
}
