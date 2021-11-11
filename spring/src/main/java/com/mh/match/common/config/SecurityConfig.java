package com.mh.match.common.config;

import com.mh.match.jwt.JwtAccessDeniedHandler;
import com.mh.match.jwt.JwtAuthenticationEntryPoint;
import com.mh.match.jwt.TokenProvider;
import com.mh.match.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// chrome://flags/#block-insecure-private-network-requests
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // swagger 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**/chat/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/project/**","/study/**","/club/**","/project","/study","/club", "/club/authority/**", "/study/authority/**", "/project/authority/**", "/mogakco").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/study/view-count/**", "/project/view-count/**", "/club/view-count/**").permitAll();
        http.csrf().disable()
//            .cors().configurationSource(corsConfigurationSource())
//            .and()
//                .formLogin().disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //OPTIONS 메소드 허락
                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/file/downloadFile/**").permitAll()
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService);
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        // - (3)
////        configuration.addAllowedOrigin("*");
//        configuration.addAllowedOriginPattern("*");
////        configuration.addAllowedOrigin("http://localhost");
////        configuration.addAllowedOrigin("https://59.151.220.195:5501");
//        // above origin is for the test @ chat
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.setAllowCredentials(true);
//
////        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        configuration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}