package com.airevents.controller;

import com.airevents.dto.request.AircraftMappingRequest;
import com.airevents.dto.request.RegisterRequest;
import com.airevents.dto.response.AircraftMappingResponse;
import com.airevents.entity.AircraftMapping;
import com.airevents.service.AircraftMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys-admin/aircraft-mapping")
public class AircraftMappingController {

    @Autowired
    private AircraftMappingService aircraftMappingService;

    @PostMapping()
    public ResponseEntity<Void> createMapping(@RequestBody AircraftMappingRequest request) {
        request.validate();
        aircraftMappingService.createMapping(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<AircraftMappingResponse>> getAll() {
        return ResponseEntity.ok(aircraftMappingService.getAllMappings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(@PathVariable("id") Long id) {
        aircraftMappingService.delete(id);
        return ResponseEntity.ok().build();
    }
}
