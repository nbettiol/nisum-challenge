package com.nisum.challenge.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.challenge.model.AppUser;
import com.nisum.challenge.model.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private AppUserRepository appUserRepository;

    public static final int EXPIRATION_TIME = 60000;
    public static final String SECRET_KEY = "SECRET";
    public static final String AUTH_KEY = "Authorization";
    public static final String BEARER_KEY = "Bearer ";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AppUser user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        String userEmail = ((User) auth.getPrincipal()).getUsername();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, EXPIRATION_TIME);

        String token = Jwts.builder().setIssuedAt(new Date())
                .setSubject(userEmail)
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        response.addHeader(AUTH_KEY, BEARER_KEY + " " + token);

        UUID id = appUserRepository.findByEmail(userEmail).get().getId();
        AppUser appUser = appUserRepository.findById(id).get();
        appUser.setLastLogin(Calendar.getInstance().getTime());
        appUser.setToken(token);
        appUserRepository.save(appUser);
    }
}