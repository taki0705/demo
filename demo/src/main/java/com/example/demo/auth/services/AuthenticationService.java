package com.example.demo.auth.services;

import com.example.demo.auth.dao.request.SignUpRequest;
import com.example.demo.auth.dao.request.SigninRequest;
import com.example.demo.auth.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
