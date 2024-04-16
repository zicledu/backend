package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ssac.LMS.domain.Tags;

import java.util.Date;

@Data
@AllArgsConstructor
public class CourseSearchResponseDto {

    private String title;
    private Tags tags;
    private String userName;
}
