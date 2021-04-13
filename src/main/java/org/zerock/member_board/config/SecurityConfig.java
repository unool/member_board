package org.zerock.member_board.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.zerock.member_board.service.CustomOAuth2UserService;
import org.zerock.member_board.service.MemberServiceImpl;
import org.zerock.member_board.service.util.AuthenticationExceptionHandler;
import org.zerock.member_board.service.util.UserLoginFailHandler;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/replies/**","/board/list","/board/read","/css/**","/login/**",
                        "/vendor/**","/modal/**","/member/**","/login/**",
                        "/review/**","/img/**","/attend/getAttend","/websocket/**",
                        "/ws_sock/**","/chat/**","/html/**","/webjars/**","/exception/**","/error/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .failureHandler(authenticationFailureHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/member/login")
                .and()
                .oauth2Login().defaultSuccessUrl("/")
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and();

        http.sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/member/login")
                .maxSessionsPreventsLogin(false);

    }

//    public void configure(AuthenticationManagerBuilder auth) throws Exception { // 9
//        auth.userDetailsService(memberService)
//                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
//                // loadUserByUsername() 구현해야함 (서비스 참고)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }

    @Bean
    public AuthenticationExceptionHandler authenticationExceptionHandler(){

        AuthenticationExceptionHandler authenticationExceptionHandler = new AuthenticationExceptionHandler();
        return authenticationExceptionHandler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler()
    {
        AuthenticationFailureHandler authenticationFailureHandler = new UserLoginFailHandler();
        return authenticationFailureHandler;
    }


}
