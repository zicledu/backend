package ssac.LMS.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterCHain(HttpSecurity http) throws Exception {

        http.csrf(auth -> auth.disable());
        http.formLogin(auth -> auth.disable());
        http.httpBasic(auth -> auth.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/join", "/login","/course/new", "/course/best").permitAll()
                .anyRequest().authenticated());

        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                                                                           @Override
                                                                           public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                                                               CorsConfiguration config = new CorsConfiguration();
                                                                               config.setAllowedOriginPatterns(Collections.singletonList("*"));
//                                                                               config.setAllowedOrigins(Collections.singletonList("*"));
                                                                               config.setAllowedMethods(Collections.singletonList("*"));
                                                                               config.setAllowCredentials(true);
                                                                               config.setAllowedHeaders(Collections.singletonList("*"));
                                                                               config.setMaxAge(3600L); //1시간
                                                                               return config;
                                                                           }
                                                                       }));
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


        );

        return http.build();
    }

}
