package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ssac.LMS.domain.Tags;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class CourseResponseDto {

    private String title;
    private String description;
    private LocalDateTime startedAt;
    private int price;
    private Tags tags;
    private String thumbnailPath;




}
