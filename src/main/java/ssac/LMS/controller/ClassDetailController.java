package ssac.LMS.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.domain.User;
import ssac.LMS.dto.CourseDetailCurriculumResponseDto;
import ssac.LMS.dto.CourseDetailSummaryResponseDto;
import ssac.LMS.dto.InstructorResponseDto;
import ssac.LMS.service.CourseDetailService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/class")
public class ClassDetailController {

    private final CourseDetailService courseDetailService;

    @GetMapping("/summary/{courseId}")
    public ResponseEntity<CourseDetailSummaryResponseDto> getSummary(@PathVariable(name = "courseId") Long courseId) {
        CourseDetailSummaryResponseDto courseSummary = courseDetailService.getCourseSummary(courseId);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(courseSummary);

    }

    @GetMapping("/curriculum/{courseId}")
    public ResponseEntity<Result> getCurriculum(@PathVariable(name = "courseId") Long courseId) {
        List<Lecture> lectures = courseDetailService.getCourseCurriculum(courseId);
        List<CourseDetailCurriculumResponseDto> curriculum = lectures.stream().map(m ->
                new CourseDetailCurriculumResponseDto(m.getLectureId(), m.getTitle(), m.getDurationMinutes(), m.getLectureOorder())).collect(Collectors.toList());

        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new Result(curriculum.size(), curriculum));
    }

    @GetMapping("/instructor/{courseId}")
    public ResponseEntity<InstructorResponseDto> getInstructor(@PathVariable(name = "courseId") Long courseId) {
        User instructor = courseDetailService.getInstructor(courseId);
        InstructorResponseDto instructorResponseDto = new InstructorResponseDto(instructor.getUserId(), instructor.getEmail(), instructor.getUserName());
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(instructorResponseDto);
    }

    @Data
    private class Result<T> {
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }
    }
}
