package com.dumblthon.messenger.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SuppressWarnings("unused")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.app.jwt.secret}")
    public String jwtSecret;

    @Value("${spring.app.jwt.ttl}")
    public long jwtTTLSec;

    @Value("${spring.app.cors.origin}")
    public String corsOrigin;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(corsOrigin);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/","/aboutus").permitAll()
//                .antMatchers("/admin/**").hasAnyRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER")
                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")  //Loginform all can access ..
//                .defaultSuccessUrl("/dashboard")
//                .failureUrl("/login?error")
//                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
//                .deleteCookies("COOKIE-BEARER")
//                .logoutSuccessHandler(
//                        (httpServletRequest, httpServletResponse, authentication) -> log.info("Logout Successful"))
//                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
//                .and()
//                .addFilterBefore(new JWTLoginFilter("/gettoken", authenticationManager(), jwtSecret, jwtTTLSec * 1000),
//                        UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JWTAuthenticationFilter(jwtSecret), UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
