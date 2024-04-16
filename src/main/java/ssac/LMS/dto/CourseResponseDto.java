package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ssac.LMS.domain.Tags;

import java.util.Date;

@Data
@AllArgsConstructor
public class CourseResponseDto {

    private String title;
    private String description;
    private Date startedAt;
    private Long price;
    private Tags tags;
    private String thumbnailPath;




}
