package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssac.LMS.domain.Tags;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailSummaryResponseDto {

    private Long courseId;
    private String title;
    private String instructorName;
    private int lectureCount;
    private String time;
    private String thumbnailPath;
    private Long price;
    private Tags tags;
}
