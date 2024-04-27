package ssac.LMS.controller;

import com.amazonaws.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.domain.User;
import ssac.LMS.dto.*;
import ssac.LMS.service.CourseDetailService;
import ssac.LMS.service.EnrollmentService;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/class")
public class ClassDetailController {

    private final CourseDetailService courseDetailService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/summary/{courseId}")
    public ResponseEntity<CourseDetailSummaryResponseDto> getSummary(@PathVariable(name = "courseId") Long courseId) {
        CourseDetailSummaryResponseDto courseSummary = courseDetailService.getCourseSummary(courseId);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(courseSummary);

    }

    @GetMapping("/curriculum/{courseId}")
    public ResponseEntity<Result> getCurriculum(@PathVariable(name = "courseId") Long courseId) {
        List<Lecture> lectures = courseDetailService.getCourseCurriculum(courseId);
        List<CourseDetailCurriculumResponseDto> curriculum = lectures.stream().map(m ->
                new CourseDetailCurriculumResponseDto(m.getLectureId(), m.getTitle(), m.getDurationMinutes(), m.getLectureOrder())).collect(Collectors.toList());

        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new Result(curriculum.size(), curriculum));
    }

    @GetMapping("/instructor/{courseId}")
    public ResponseEntity<InstructorResponseDto> getInstructor(@PathVariable(name = "courseId") Long courseId) {
        log.info("courseId={}", courseId);
        HashMap<String, Object> instructor = courseDetailService.getInstructor(courseId);
        User user = (User) instructor.get("user");
        int count = (int) instructor.get("count");
        InstructorResponseDto instructorResponseDto = new InstructorResponseDto(user.getUserId(), user.getUserName(), user.getEmail(), count);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(instructorResponseDto);
    }

    @GetMapping("/info/{courseId}")
    public ResponseEntity<GetInfoResponseDto> getInfo(@PathVariable(name = "courseId") Long courseId) {
        String markdown = courseDetailService.getInfo(courseId);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new GetInfoResponseDto(markdown));
    }

    @PostMapping("/enrollment")
    public ResponseEntity<CreateEnrollmentResponseDto> createEnrollment(@RequestBody CreateEnrollmentRequestDto createEnrollmentRequestDto) {
        Long savedEnrollmentId = enrollmentService.save(createEnrollmentRequestDto);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(new CreateEnrollmentResponseDto(savedEnrollmentId));
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
