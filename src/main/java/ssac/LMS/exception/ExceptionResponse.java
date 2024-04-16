package ssac.LMS.exception;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ExceptionResponse {

    private String code;
    private String message;
}
