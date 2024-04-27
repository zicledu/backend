package ssac.LMS.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssac.LMS.domain.Course;
import ssac.LMS.dto.MyLectureResponseDto;
import ssac.LMS.service.CourseListService;
import ssac.LMS.service.LectureService;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
@Slf4j
public class ClassroomController {
    private final CourseListService courseListService;
    private final LectureService lectureService;

    @Data
    static class Result<T> {
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }
    }

    @GetMapping("/{userId}/{courseId}")
    public ResponseEntity<?> getMyClass(@PathVariable(name = "userId") String userId,
                                        @PathVariable(name = "courseId") Long courseId,
                                        @AuthenticationPrincipal Jwt jwt) {

        // jwt에서 받은 사용자 ID와 요청으로 받은 사용자 ID를 비교하여 일치하지 않으면 UNAUTHORIZED 상태를 반환합니다.
        if (!userId.equals(jwt.getClaim("cognito:username").toString())) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Id가 맞지 않습니다.");
        }
        log.info("jwtUserId={}", jwt.getClaim("cognito:username").toString());
        // 검증이 끝나면 해당하는 사용자의 수강 내역을 가져옵니다.
        boolean isExist = courseListService.getEnrollment(userId, courseId);
        if (!isExist) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("권한이 없습니다.");
        }

        //courseId를 사용하여 Course 객체를 가져옵니다.
        Course course = courseListService.getEnrollmentCourse(courseId);
        log.info("course: {}", course);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 강의가 존재하지 않습니다.");
        }

        //course를 기준으로 해당하는 lecture 모두 가져오기
        List<MyLectureResponseDto> responseDtoList = lectureService.getLecturesByCourse(course).stream()
                .map(lecture -> new MyLectureResponseDto(lecture.getCourse().getCourseId(), lecture.getTitle(), lecture.getLectureOrder(), lecture.getDurationMinutes(), lecture.getVideoPath720(), lecture.getVideoPath1080(), lecture.getVideoPathOriginal(), lecture.getCourse().getThumbnailPath()))
                .collect(Collectors.toList());
        log.info("responseDtoList: {}", responseDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(responseDtoList.size(), responseDtoList));
    }

}