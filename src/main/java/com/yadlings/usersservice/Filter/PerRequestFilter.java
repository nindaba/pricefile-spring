package com.yadlings.usersservice.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PerRequestFilter extends OncePerRequestFilter {
    private String secret;
    public PerRequestFilter(String secret){
        this.secret = secret;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getParameter("Authorization");
        if(httpServletRequest.getServletPath().matches("/price-check/login")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else{
            String token = httpServletRequest.getHeader("Authorization");
            if(token != null && token.startsWith("bearer ")){
                try{
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    token = token.substring("bearer ".length());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT verify = verifier.verify(token);
                    String username = verify.getSubject().split(" ")[0];
                    String[] roles = verify
                            .getClaims()
                            .get("Roles")
                            .asArray(String.class);
                    List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roles);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorityList);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(httpServletRequest,httpServletResponse);
                }
                catch (Exception x){
                    httpServletResponse.sendError(1,"Invalid Token");
                    x.printStackTrace();
                }
            }
            else {
                filterChain.doFilter(httpServletRequest,httpServletResponse);
            }
        }
    }
}
