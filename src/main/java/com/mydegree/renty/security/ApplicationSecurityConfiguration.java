package com.mydegree.renty.security;

import com.mydegree.renty.jwt.*;
import com.mydegree.renty.service.abstracts.ILoginService;
import com.mydegree.renty.service.abstracts.IUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
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
    private final IUserService userService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public ApplicationSecurityConfiguration(PasswordEncoder passwordEncoder,
                                            ILoginService loginService,
                                            IUserService userService, JwtConfig jwtConfig,
                                            SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.loginService = loginService;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey, userService))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/admin/**", "/owner/**", "/renter/**", "/anon/**").hasAnyAuthority(ApplicationUserRole.ADMIN.name())
                .antMatchers("/owner/**", "/renter/**", "/anon/**").hasAnyAuthority(ApplicationUserRole.OWNER.name(), ApplicationUserRole.ADMIN.name())
                .antMatchers("/renter/**", "/anon/**", "/payment/**").hasAnyAuthority(ApplicationUserRole.RENTER.name(),
                                                                                    ApplicationUserRole.ADMIN.name(),
                                                                                    ApplicationUserRole.RENTER.name())
                .antMatchers("/anon/**", "/h2-console/**").anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .logout();
        http
                .headers().frameOptions().disable(); //for h2-console
        http
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPointHandler()) //for unauthorized entry point (error 401)
                .accessDeniedHandler(accessDeniedHandler()); //for access denied entry point (error 403)

        if (activeProfile.equals("prod")) {
//            http
//                    .requiresChannel()
//                    .anyRequest()
//                    .requiresSecure();
        }
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
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    public CustomUnauthorizedEntryPointHandler unauthorizedEntryPointHandler() {
        return new CustomUnauthorizedEntryPointHandler();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
