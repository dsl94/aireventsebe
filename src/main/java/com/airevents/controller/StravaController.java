package com.airevents.controller;

import com.airevents.dto.request.StravaCodeRequest;
import com.airevents.dto.response.StravaCredentials;
import com.airevents.dto.response.StravaResponse;
import com.airevents.dto.response.UserResponse;
import com.airevents.security.dto.JwtResponse;
import com.airevents.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/strava")
public class StravaController {

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> loginWithStrava(@RequestBody StravaResponse body) throws IOException {
//        JwtResponse jwtResponse = new JwtResponse();
//        String urlString = "https://www.strava.com/oauth/token?client_id=116659&client_secret=f2699193cc92b28b75a0db52fcb522704f637121&code="+body.getCode()+"&grant_type=authorization_code";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        try {
//
//            HttpPost request = new HttpPost(urlString);
//
//            CloseableHttpResponse response = httpClient.execute(request);
//
//            try {
//
//                // Get HttpResponse Status
//                System.out.println(response.getProtocolVersion());              // HTTP/1.1
//                System.out.println(response.getStatusLine().getStatusCode());   // 200
//                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
//                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK
//
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    // return it as a String
//                    String result = EntityUtils.toString(entity);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                    StravaResponse stravaResponse = objectMapper.readValue(result, StravaResponse.class);
//                    jwtResponse = userService.createStravaAccountAndOrLogin(stravaResponse);
//                }
//
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpClient.close();
//        }
//        if (jwtResponse.getToken() == null) {
//            throw new UsernameNotFoundException("Greska sa prijavom");
//        }
        JwtResponse jwtResponse = userService.createStravaAccountAndOrLogin(body);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/credentials")
    public ResponseEntity<StravaCredentials> getCredentials() {
        return ResponseEntity.ok(new StravaCredentials("116659", "f2699193cc92b28b75a0db52fcb522704f637121"));
    }
}
