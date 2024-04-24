package ssac.LMS.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GlobalSignOutRequest;
import ssac.LMS.exception.CognitoAuthenticationEntryPoint;
import ssac.LMS.service.CognitoJoinServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
    private final JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
    private final EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;
    @Value("${spring.security.oauth2.client.provider.cognito.jwk-set-uri}")
    URL jwkSetUri;

    @Value("${simple.jwe-key-value}")
    RSAPrivateKey key;

    private final CognitoJoinServiceImpl cognitoJoinService;

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

                        .requestMatchers("/", "/join", "/login", "/course/new", "/course/best", "/refresh", "/course/**", "/class/**", "/upload/**").permitAll()
                        .requestMatchers("/upload/**").hasAuthority("ROLE_INSTRUCTOR")
                        .requestMatchers("/main").hasAuthority("ROLE_STUDENT")
                        .anyRequest().authenticated())
                .exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(new CognitoAuthenticationEntryPoint()));
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()
                        )));

        http.logout(
                auth -> auth.logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .logoutSuccessHandler((request, response, authenticaton) -> {
                            String authorization = request.getHeader("Authorization");
                            if (authorization != null && authorization.contains("Bearer")) {
                                String accessToken = authorization.replace("Bearer ", "").trim();
                                CognitoIdentityProviderClient cognitoClient = cognitoJoinService.getCognitoClient();
                                GlobalSignOutRequest globalSignOutRequest = GlobalSignOutRequest.builder()
                                        .accessToken(accessToken)
                                        .build();
                                cognitoClient.globalSignOut(globalSignOutRequest);
                                response.setStatus(HttpServletResponse.SC_OK); // 응답의 상태 코드를 설정 (예: 200 OK)
                                response.setContentType("text/plain"); // 응답의 컨텐츠 타입 설정
                                response.setCharacterEncoding("UTF-8"); // 응답의 문자 인코딩 설정
                                response.getWriter().write("Custom logout message"); // 응답의 본문을 작성하여 클라이언트에게 전달
                                response.getWriter().flush(); // 작성한 응답을 클라이언트에게 전송

                            } else {
                                // 인증되지 않은 사용자의 경우 처리
                                // ...
                                try {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태 코드 설정
                                    response.getWriter().write("Not Login"); // 메시지 전송
                                    response.getWriter().flush();
                                } catch (IOException e) {
                                    // 응답을 보낼 때 오류가 발생한 경우 처리
                                    e.printStackTrace();
                                }
                            }
                        }));

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
    @Bean
    JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(jwtProcessor());
    }

    private JWTProcessor<SecurityContext> jwtProcessor() {
        JWKSource<SecurityContext> jwsJwkSource = new RemoteJWKSet<>(this.jwkSetUri);
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(this.jwsAlgorithm,
                jwsJwkSource);

        JWKSource<SecurityContext> jweJwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey()));
        JWEKeySelector<SecurityContext> jweKeySelector = new JWEDecryptionKeySelector<>(this.jweAlgorithm,
                this.encryptionMethod, jweJwkSource);

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWEKeySelector(jweKeySelector);

        return jwtProcessor;
    }

    private com.nimbusds.jose.jwk.RSAKey rsaKey() {
        RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) this.key;
        Base64URL n = Base64URL.encode(crtKey.getModulus());
        Base64URL e = Base64URL.encode(crtKey.getPublicExponent());

        return new RSAKey.Builder(n, e).privateKey(this.key).keyUse(KeyUse.ENCRYPTION).build();
    }
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new MyCustomAuthoritiesConverter());
        return converter;
    }

    // Custom converter to extract "cognito:role" claim
    private static class MyCustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            Object claim = jwt.getClaim("custom:role");
            String role = claim.toString();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

            return authorities;
        }
    }

}
