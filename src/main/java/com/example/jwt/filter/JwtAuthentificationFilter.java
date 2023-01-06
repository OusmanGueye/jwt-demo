package com.example.jwt.filter;

import com.example.jwt.config.JwtUtil;
import com.example.jwt.service.CustomeUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomeUserDetailsService customeUserDetailsService;

    private String token;
    private String username;

    public JwtAuthentificationFilter(JwtUtil jwtUtil, CustomeUserDetailsService customeUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customeUserDetailsService = customeUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){

             token = bearerToken.substring(7);

            try {

                username = jwtUtil.extractUsername(token);
                UserDetails userDetails = customeUserDetailsService.loadUserByUsername(username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    upa.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upa);
                }else {
                    System.out.println("invalid token");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            System.out.println("invalid bearer token format");
        }

        filterChain.doFilter(request, response);
    }
}
