package ssac.LMS.jwt;

import com.nimbusds.jose.Header;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.oauth2.jwt.Jwt;
import ssac.LMS.domain.Role;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    @Value("${spring.security.oauth2.client.provider.cognit.jwk-set-uri}")
    private String JWKS_URL;
    @Test
    void verifyToken() throws ParseException, MalformedURLException {

        String idToken = "eyJraWQiOiJqSGtrTVJtSU12TXBROVpQMjNEUTBnODFkNEN6Zk9meDZVTk5helVLdjVJPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIyNDk4ZjQ5OC1mMDYxLTcwNDUtZjE3ZS1lMTQwMzg2M2E1N2UiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfQ3V6eHczVFo5IiwiY29nbml0bzp1c2VybmFtZSI6IjI0OThmNDk4LWYwNjEtNzA0NS1mMTdlLWUxNDAzODYzYTU3ZSIsIm9yaWdpbl9qdGkiOiI4Njk1OGU1Yi0wNGZhLTQ0NGUtYWRkMi05NjJkODRmZDBmMjAiLCJhdWQiOiI3NzllMmNnZDExc2k1bzg3YWRycmU4OG1tcCIsImV2ZW50X2lkIjoiNDM3YzcxYTQtNDI5OS00YmRkLWE5ZDEtZDIwNWY4ODVjMmZlIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE3MTI3NzcwOTMsImV4cCI6MTcxMjc4MDY5MywiY3VzdG9tOnJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNzEyNzc3MDkzLCJqdGkiOiIxYTc1NGExMS1mMDIwLTQ0YTktOWRkZS0zYzkwNzYyNmRiZDQiLCJlbWFpbCI6InNqaHR5MTIzQG5hdmVyLmNvbSJ9.IsDky7Ra8jssNA-Le4WrJsUTFwYkPf9HYFJT7uQ8pQscnFOIfyQONjc6KjinJ40kuHgm0Mj7naTdsnhq8ynRPqeorXGvMTPn1OwWgQ_-pI_HPzX2UoZZngPetRMcuyNKn_P0rnmEKQdKt5vFYyJcdhnHpMYLLAOejUuvPOBXZX_ZOPslKNxeUx797lyw2nHlfRjbhUKZ4pUmFMSu9mxqydjt5l54yOumIP7Gk-1H2-vIesfMIqo7tpRbn3MSPnBenXumusrtdO1XPRHjr66YnjetjtXo87D1TZp3OsVLOQ3vZQob_XtDerSDYn3N6j2Pvkj13rBjU7joKff3qu8iJg";

        Map<String, Object> claims = JWTParser.parse(idToken).getJWTClaimsSet().getClaims();
//        String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("custom:role").toString();
        boolean before = JWTParser.parse(idToken).getJWTClaimsSet().getExpirationTime().before(new Date());
        String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("email").toString();
        System.out.println("before = " + before);
        System.out.println("header = " + claims);
        System.out.println("role = " + role);

        System.out.println(Role.fromValue("student"));
    }

}