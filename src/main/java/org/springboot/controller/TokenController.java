package org.springboot.controller;

import org.springboot.entities.RefreshToken;
import org.springboot.model.UserInfoDto;
import org.springboot.request.AuthRequestDto;
import org.springboot.request.RefreshTokenRequestDto;
import org.springboot.response.JwtResponseDto;
import org.springboot.services.JwtService;
import org.springboot.services.RefreshTokenService;
import org.springboot.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignedUp = userDetailService.signupUser(userInfoDto);

            if(Boolean.FALSE.equals(isSignedUp)) {
                return new ResponseEntity<>("Already Exists", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(
                    JwtResponseDto
                            .builder()
                            .accessToken(jwtToken)
                            .token(refreshToken.getToken())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("auth/v1/login")
    public ResponseEntity authenticatedAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );

        if(authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());

            return new ResponseEntity<>(
                    JwtResponseDto
                            .builder()
                            .accessToken(jwtService.generateToken(authRequestDto.getUsername()))
                            .token(refreshToken.getToken())
                            .build(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map( userInfo -> {
                        String accessToken = jwtService.generateToken(userInfo.getUsername());

                        return JwtResponseDto.builder()
                                .accessToken(accessToken)
                                .token(refreshTokenRequestDto.getToken())
                                .build();
                    }
                )
                .orElseThrow(() -> new RuntimeException("Refresh token not found in DB"));
    }
}
