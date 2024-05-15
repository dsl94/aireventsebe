package com.airevents.service;

import com.airevents.dto.mapper.UserMapper;
import com.airevents.dto.request.ChangePasswordRequest;
import com.airevents.dto.request.CreateUserRequest;
import com.airevents.dto.request.GuestConvertRequest;
import com.airevents.dto.request.UpdateUserRequest;
import com.airevents.dto.response.StravaResponse;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Role;
import com.airevents.entity.User;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.RoleRepository;
import com.airevents.repository.UserRepository;
import com.airevents.security.JwtTokenUtil;
import com.airevents.security.RolesConstants;
import com.airevents.security.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {

    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<User> members = new ArrayList<>();
        for (User u: users) {
            for (Role r: u.getRoles()) {
                if (!r.getRole().equals(RolesConstants.ROLE_GUEST.name())) {
                    members.add(u);
                    break;
                }
            }
        }
        return UserMapper
                .entityToResponse(
                        members
                );
    }

    public List<UserResponse> guests() {
        List<User> users = userRepository.findAll();
        List<User> members = new ArrayList<>();
        for (User u: users) {
            for (Role r: u.getRoles()) {
                if (r.getRole().equals(RolesConstants.ROLE_GUEST.name())) {
                    members.add(u);
                    break;
                }
            }
        }
        return UserMapper
                .entityToResponse(
                        members
                );
    }

    public void createUser(CreateUserRequest request) {
        User user = UserMapper.registerRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Validate users email for duplicates
        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (userByEmail.isPresent()) {
            throw new RcnException(ErrorCode.USER_EXIST, "Korisnik sa tim emailom vec postoji");
        }

        Role role = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    public void convertGuest(Long id, GuestConvertRequest request) {
        Role roleUser = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());
        User user = userRepository.findById(id).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
        user.setEmail(request.getEmail());
        user.setMembershipUntil(LocalDate.parse(request.getMembershipUntil(), REQUEST_DATE_FORMAT).atStartOfDay());
        userRepository.save(user);
    }

    public JwtResponse createStravaAccountAndOrLogin(StravaResponse stravaResponse) {
        Role roleGuest = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_GUEST.name());
        String username = stravaResponse.getAthlete().getFirstname() + "_" + stravaResponse.getAthlete().getLastname() + "_" + stravaResponse.getAthlete().getId();
        User user = userRepository.findByStravaIdIgnoreCase(String.valueOf(stravaResponse.getAthlete().getId()));
        if (user != null) {
            return login(user);
        } else {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("S3cr3t"));
            user.setFullName(stravaResponse.getAthlete().getFirstname() + " " + stravaResponse.getAthlete().getLastname());
            user.setGender(stravaResponse.getAthlete().getSex());
            user.setStravaId(String.valueOf(stravaResponse.getAthlete().getId()));
            user.setStravaRefreshToken(stravaResponse.refresh_token);
            user.setRoles(Set.of(roleGuest));
            user = userRepository.save(user);
            return login(user);
        }
    }

    private JwtResponse login(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), "S3cr3t"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        user.setLastLoginDate(LocalDateTime.now());
        userRepository.saveAndFlush(user);

        return new JwtResponse(token, user.getId(), dateFormat.format(expiration), user.getEmail(), user.getFullName(), roles, true, user.getStravaRefreshToken() != null && !user.getStravaRefreshToken().isEmpty());
    }

    public UserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        user.setFullName(request.getFullName());
        user.setShirtSize(request.getShirtSize());
        user.setPhone(request.getPhone());
        user.setInfo(request.getInfo());
        if (request.getMedicalUntil() != null) {
            user.setMembershipUntil(LocalDate.parse(request.getMembershipUntil(), REQUEST_DATE_FORMAT).atStartOfDay());
        }

        if (request.getMedicalUntil() != null) {
            user.setMedicalUntil(LocalDate.parse(request.getMedicalUntil(), REQUEST_DATE_FORMAT).atStartOfDay());
        }

        return UserMapper.entityToResponse(userRepository.save(user));
    }

    public UserResponse updateProfile(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsernameIgnoreCase(username);

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setStravaId(request.getStravaId());
        user.setShirtSize(request.getShirtSize());
        user.setPhone(request.getPhone());
        user.setInfo(request.getInfo());

        return UserMapper.entityToResponse(userRepository.save(user));
    }

    public UserResponse updateStrava(String username, StravaResponse stravaResponse) {
        User user = userRepository.findByUsernameIgnoreCase(username);

        user.setStravaId(String.valueOf(stravaResponse.getAthlete().getId()));
        user.setStravaRefreshToken(stravaResponse.refresh_token);

        return UserMapper.entityToResponse(userRepository.save(user));
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsernameIgnoreCase(username);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public UserResponse getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        return UserMapper.entityToResponse(user);
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        userRepository.delete(user);
    }

    public UserResponse getUserProfile(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username);

        return UserMapper.entityToResponse(user);
    }
}
