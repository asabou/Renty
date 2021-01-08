package com.mydegree.renty.security;

import com.mydegree.renty.jwt.CustomUnauthorizedEntryPoint;
import com.mydegree.renty.jwt.JwtConfig;
import com.mydegree.renty.jwt.JwtTokenVerifierFilter;
import com.mydegree.renty.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.mydegree.renty.service.ILoginService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ILoginService loginService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public ApplicationSecurityConfiguration(PasswordEncoder passwordEncoder,
                                            ILoginService loginService,
                                            JwtConfig jwtConfig,
                                            SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.loginService = loginService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/admin/**", "/owner/**", "/renter/**", "/anon/**").hasAnyAuthority(ApplicationUserRole.ADMIN.name())
                .antMatchers("/owner/**", "/renter/**", "/anon/**").hasAnyAuthority(ApplicationUserRole.PLACE_OWNER.name())
                .antMatchers("/renter/**", "/anon/**").hasAnyAuthority(ApplicationUserRole.RENTER.name())
                .antMatchers("/anon/**", "/h2-console/**").anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .logout();
        http
                .headers().frameOptions().disable(); //for h2-console
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()); //for unauthorized entry point
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(loginService);
        return provider;
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "POST", "PUT", "DELETE", "PATCH", "GET");
            }
        };
    }

    @Bean
    public CustomUnauthorizedEntryPoint authenticationEntryPoint() {
        return new CustomUnauthorizedEntryPoint();
    }
}
