package com.learnweb.learnweb.controller;


import com.learnweb.learnweb.dto.request.ApiResponse;
import com.learnweb.learnweb.dto.request.AuthenticationRequest;
import com.learnweb.learnweb.dto.request.IntrosoectRequest;
import com.learnweb.learnweb.dto.response.AuthenticationResponse;
import com.learnweb.learnweb.dto.response.IntrosoectRespone;
import com.learnweb.learnweb.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrosoectRespone> authenticate(@RequestBody IntrosoectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrosoectRespone>builder()
                .result(result)
                .build();
    }

}
