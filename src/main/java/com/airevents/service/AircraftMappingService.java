package com.airevents.service;

import com.airevents.dto.mapper.AircraftMappingMapper;
import com.airevents.dto.request.AircraftMappingRequest;
import com.airevents.dto.response.AircraftMappingResponse;
import com.airevents.error.AirEventsException;
import com.airevents.error.ErrorCode;
import com.airevents.repository.AircraftMappingRepository;
import com.airevents.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AircraftMappingService {

    @Autowired
    private AircraftMappingRepository mappingRepository;
    @Autowired
    private AircraftRepository aircraftRepository;

    public List<AircraftMappingResponse> getAllMappings() {
        return mappingRepository
                .findAll()
                .stream()
                .map(AircraftMappingMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public void createMapping(AircraftMappingRequest request) {
        // Validate that that sim key does not exist
        if (mappingRepository.findBySimKeyIgnoreCase(request.getSimKey()).isPresent()) {
            throw new AirEventsException(ErrorCode.KEY_EXISTS, "Given sim key already exist");
        }
        // Validate that icao aircraft exist
        if (aircraftRepository.findByIcaoIgnoreCase(request.getIcao()).size() == 0) {
            throw new AirEventsException(ErrorCode.AIRCRAFT_NOT_FOUND, "Given aircraft ICAO code does not exist");
        }
        // Save
        mappingRepository.save(AircraftMappingMapper.mapToEntity(request));
    }
}
