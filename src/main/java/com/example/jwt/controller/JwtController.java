package com.example.jwt.controller;

import com.example.jwt.config.JwtUtil;
import com.example.jwt.service.CustomeUserDetailsService;
import com.example.jwt.util.Request;
import com.example.jwt.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    private final JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final CustomeUserDetailsService service;

    public JwtController(JwtUtil jwtUtil,CustomeUserDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

   @PostMapping("/generateToken")
    public ResponseEntity<?> generateTokens(@RequestBody Request request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = service.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new Response(token));
    }
}
