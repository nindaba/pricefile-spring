package com.yadlings.usersservice.Security;

import com.yadlings.usersservice.Filter.AuthFilter;
import com.yadlings.usersservice.Filter.BearerTokenFilter;
//import com.yadlings.usersservice.Service.UserSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private UserSecurityService userSecurityService;
//    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userSecurityService)
//                .passwordEncoder(passwordEncoder());
    }
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Value("${jwt.secret}")
    private String secret;
    @Value("${cors.age}")
    private long maxAge;
    @Value("${origins}")
    private List origins;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AuthFilter authFilter = new AuthFilter();
        authFilter.setFilterProcessesUrl("/userapi/login");
        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(secret);

        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/userapi/users","/userapi/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(bearerTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(authFilter);
    }
//    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(origins);
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setMaxAge(maxAge);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}
