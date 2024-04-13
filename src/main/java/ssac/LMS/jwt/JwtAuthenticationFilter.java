//package ssac.LMS.jwt;
//
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.jwk.source.RemoteJWKSet;
//import com.nimbusds.jose.proc.JWSVerificationKeySelector;
//import com.nimbusds.jose.proc.SecurityContext;
//import com.nimbusds.jose.proc.SimpleSecurityContext;
//import com.nimbusds.jwt.JWT;
//import com.nimbusds.jwt.JWTParser;
//import com.nimbusds.jwt.PlainJWT;
//import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
//import com.nimbusds.jwt.proc.DefaultJWTProcessor;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
//import ssac.LMS.domain.Role;
//import ssac.LMS.domain.User;
//
//import java.io.IOException;
//import java.net.URL;
//import java.text.ParseException;
//
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtils jwtUtils;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorization = request.getHeader("Authorization");
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        String token = authorization.split(" ")[1];
//
//        try {
//            if (jwtUtils.isExpired(authorization)) {
//                System.out.println("token expired");
//                filterChain.doFilter(request, response);
//
//                return;
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            String email = jwtUtils.getEmail(authorization);
//            String role = jwtUtils.getRole(authorization);
//            User user = new User();
//            user.setEmail(email);
//            user.setRole(Role.fromValue(role));
//
//        jwtUtils.getAu
//
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
