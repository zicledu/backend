package ssac.LMS.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotConfirmedException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> httpClientErrorException(HttpClientErrorException e) {

        log.info("httpClientError={}", e.getStatusText());

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getStatusCode().toString(), e.getStatusText());

        return ResponseEntity.status(e.getStatusCode()).body(exceptionResponse);
    }

    @ExceptionHandler(CognitoIdentityProviderException.class)
    public ResponseEntity<ExceptionResponse> userNotConfirmedException(CognitoIdentityProviderException e) {

        log.info("userNotConfirmedException={}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.toString(), e.getMessage());
        return ResponseEntity.status(e.statusCode()).body(exceptionResponse);
    }

}
