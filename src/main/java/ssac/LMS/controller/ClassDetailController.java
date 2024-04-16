package ssac.LMS.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssac.LMS.domain.Course;
import ssac.LMS.dto.CourseDetailSummaryResponseDto;
import ssac.LMS.service.CourseDetailService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/class")
public class ClassDetailController {

    private final CourseDetailService courseDetailService;

    @GetMapping("/summary/{courseId}")
    public ResponseEntity<CourseDetailSummaryResponseDto> getSummary(@PathVariable Long courseId) {
        CourseDetailSummaryResponseDto courseSummary = courseDetailService.getCourseSummary(courseId);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(courseSummary);
    }
}
