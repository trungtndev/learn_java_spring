package com.learnweb.learnweb.service;


import com.learnweb.learnweb.dto.request.AuthenticationRequest;
import com.learnweb.learnweb.dto.request.IntrosoectRequest;
import com.learnweb.learnweb.dto.response.AuthenticationResponse;
import com.learnweb.learnweb.dto.response.IntrosoectRespone;
import com.learnweb.learnweb.entity.User;
import com.learnweb.learnweb.exceptoion.AppException;
import com.learnweb.learnweb.exceptoion.ErrorCode;
import com.learnweb.learnweb.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenticationService {
    UserRepository userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    public IntrosoectRespone introspect(IntrosoectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);
        return IntrosoectRespone.builder()
                .valid(verified && expiration.after(new Date()))
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {

        var user = userRepository.findByUsername(request.getUsernname())
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_EXITED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated){
            throw new AppException(ErrorCode.UNTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .isAuthenticate(authenticated)
                .token(token)
                .build();
    }

    private String generateToken(User user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("learnweb")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token " + e);
            throw new RuntimeException(e);
        }
    }


    public String buildScope(User user){
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(joiner::add);
        }
        return joiner.toString();
    }
}
