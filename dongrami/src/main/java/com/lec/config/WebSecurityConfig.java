package com.lec.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lec.entity.Member;
import com.lec.service.LoginService;
import com.lec.service.Oauth2UserServiceImplement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

   
    @Autowired
    Oauth2UserServiceImplement oAuth2UserService;

       
    @Autowired
    LoginService loginService;

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .csrf().disable()
               .authorizeHttpRequests(request -> request
            	.requestMatchers("/review").authenticated()
                  .requestMatchers("/api/replies/{voteId}").authenticated()
                   .anyRequest().permitAll()
               )
               .formLogin(form -> form
                   .loginPage("/login")
                   .loginProcessingUrl("/auth/login")
                   .defaultSuccessUrl("/", true)  // index 페이지로 리다이렉트, true는 항상 이 URL로 리다이렉트함을 의미
                   .failureUrl("/login?error")
                   .permitAll()
               )
               .oauth2Login(oauth2 -> oauth2
                   .loginPage("/login")
                   .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                   .userInfoEndpoint(userInfo -> userInfo
                       .userService(oAuth2UserService)
                   )
                   .successHandler(this::onAuthenticationSuccess)
               )
               .logout(logout -> logout
                   .logoutUrl("/auth/logout")
                   .logoutSuccessUrl("/login")
                   .invalidateHttpSession(true)
               );

           return http.build();
       }

       private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            
            String email = null;
            String name = null;
            
            switch (provider) {
            case "google":
               email = oAuth2User.getAttribute("email");
               break;
            case "naver":
               Map<String, Object> responseMap = (Map<String, Object>) oAuth2User.getAttribute("response");
               System.out.println("naver_response: " + responseMap);
               if (response != null) {
                  email = (String) responseMap.get("email");
                  System.out.println("config_naver_email : " + email);
               }
               break;
            case "kakao":
               Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
               System.out.println("kakao_reponse : " + kakaoAccount);
               if (kakaoAccount != null) {
                  email = (String) kakaoAccount.get("email");
                  System.out.println("config_kakao_email : " + email);
                  //Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
               }
               break;
            }
            
            if (email == null) {
            // 이메일을 가져올 수 없는 경우 처리
            response.sendRedirect("/login?error=email_not_provided");
            return;
            }
            
            Member member = loginService.authenticateSocialUser(email, provider);
            
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", member);
//            session.setAttribute("userId", member.getUserId());
//            session.setAttribute("email", email);
//            session.setAttribute("provider", provider);
            response.sendRedirect("/");
      }
       
       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }
}
 
