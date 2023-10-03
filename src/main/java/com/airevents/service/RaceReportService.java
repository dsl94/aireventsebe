package com.airevents.service;

import com.airevents.dto.mapper.RaceReportMapper;
import com.airevents.dto.request.CreateRaceReportRequest;
import com.airevents.dto.response.RaceReportResponse;
import com.airevents.entity.RaceReport;
import com.airevents.entity.Role;
import com.airevents.entity.User;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.RaceReportRepository;
import com.airevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceReportService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RaceReportRepository raceReportRepository;

    public List<RaceReportResponse> all(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("ROLE_ADMIN") || role.getRole().equals("ROLE_SYSTEM_ADMIN")) {
                isAdmin = true;
            }
        }
        if (isAdmin) {
            return getAllRaceReports();
        }

        return getAllRaceReportsForUsers(user);
    }

    public List<RaceReportResponse> getAllRaceReports() {
        return raceReportRepository.findAllByOrderByDateOfRaceAsc()
                .stream()
                .map(RaceReportMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<RaceReportResponse> getAllRaceReportsForUsers(User user) {
        return raceReportRepository.findAllByUserOrderByDateOfRaceAsc(user)
                .stream()
                .map(RaceReportMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public void createRaceReport(CreateRaceReportRequest request, String username) {
        RaceReport report = RaceReportMapper.requestToEntity(request);
        User user = userRepository.findByUsernameIgnoreCase(username);
        report.setUser(user);

        raceReportRepository.save(report);
    }

    public void deleteRaceReport(Long id) {
        RaceReport report = raceReportRepository.findById(id)
                .orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Trka nije pronadjena"));

        raceReportRepository.delete(report);
    }
}
