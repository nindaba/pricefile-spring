package com.yadlings.usersservice.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BearerTokenFilter extends OncePerRequestFilter {
    private String secret;

    public BearerTokenFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getServletPath().matches("/userapi/login")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            final String bearer = httpServletRequest.getHeader("Authorization");
            if (StringUtils.startsWithIgnoreCase(bearer, "bearer ")) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    final String token = bearer.substring("bearer ".length());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT verify = verifier.verify(token);
                    String username = verify.getSubject().split(" ")[0];

                    Collection<? extends GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_BASIC"));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,roles);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                } catch (Exception x) {
                    httpServletResponse.sendError(1, "Invalid Token");
                    x.printStackTrace();
                }
            } else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
    }
}
