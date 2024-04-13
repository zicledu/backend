package ssac.LMS.jwt;

import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class JwtUtils {

    public Boolean isExpired(String token) throws ParseException {
        boolean before = JWTParser.parse(token).getJWTClaimsSet().getExpirationTime().before(new Date());
        return before;
    }

    public String getRole(String idToken) throws ParseException {
        String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("custom:role").toString();
        return role;
    }

    public String getEmail(String idToken) throws ParseException {
        String role = JWTParser.parse(idToken).getJWTClaimsSet().getClaim("email").toString();
        return role;
    }

}
