package ssac.LMS.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Enrollment;
import ssac.LMS.dto.MyCourseResponseDto;
import ssac.LMS.dto.MyEnrollmentResponseDto;
import ssac.LMS.service.CourseListService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {
    private CourseListService courseListService;
    private MyEnrollmentResponseDto myEnrollmentResponseDto;

    @Data
    static class Result<T> {
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }
    }

    @GetMapping("/{userId}/{courseId}/{enrollmentId}")
    public ResponseEntity<?> getMyEnrollment(@PathVariable(name = "userId") String userId,
                                             @PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "enrollmentId") Long enrollmentId,
                                             @AuthenticationPrincipal Jwt jwt) {

        // jwt에서 받은 사용자 ID와 요청으로 받은 사용자 ID를 비교하여 일치하지 않으면 UNAUTHORIZED 상태를 반환합니다.
        if (!userId.equals(jwt.getClaim("cognito:username").toString())) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Id가 맞지 않습니다.");
        }

        // 검증이 끝나면 해당하는 사용자의 수강 내역을 가져옵니다.
        List<Enrollment> myEnrollments = courseListService.getEnrollments(userId, courseId, enrollmentId);

        // 사용자의 수강 내역이 비어있는지 확인합니다.
        if (myEnrollments.isEmpty()) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("수강 내역이 없습니다.");
        }

        // 수강 내역에 대한 처리를 진행합니다.
        // ...

        // 수강 내역에 대한 응답을 반환합니다.
        return ResponseEntity.ok().body(myEnrollments);
    }
}



//
//    @GetMapping("/{userId}")
//    public ResponseEntity<?> getMyEnrollment(@PathVariable(name = "userId") String userId, @AuthenticationPrincipal Jwt jwt) {
//        log.info("getUserId={}", userId);
//        log.info("jwtUserId={}", jwt.getClaim("cognito:username").toString());
//
//        // jwt에서 받은 사용자 ID와 요청으로 받은 사용자 ID를 비교하여 일치하지 않으면 UNAUTHORIZED 상태를 반환합니다.
//        if (!userId.equals(jwt.getClaim("cognito:username").toString())) {
//            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Id가 맞지 않습니다.");
//        }
//        //enrollment 테이블에서 해당 사용자의 수강 내역 가져오기
//        List<Enrollment> myClass = courseListService.getMyClass(userId);
//
//        // 사용자의 수강 내역이 비어있는지 확인합니다.
//        if (myClass.isEmpty()) {
//            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("수강 내역이 없습니다.");
//        }
//
////        // 수강 내역이 있다면 각 수강 내역의 course_id를 가져와서 해당하는 강좌 정보를 불러옵니다.
////        List<MyEnrollmentResponseDto> myEnrollmentResponseDto = new ArrayList<>();
////        for (Enrollment enrollment : myClass) {
////            // 강좌 정보를 불러오는 메서드를 호출하여 강좌 정보를 가져옵니다.
////
////            Course course = courseListService.getCourseById(enrollment.getCourseId());
////            // 강좌 정보가 null이 아니라면 DTO로 변환하여 리스트에 추가합니다.
////            if (course != null) {
////                MyEnrollmentResponseDto dto = new MyEnrollmentResponseDto(
////                        course.getCourseId(),
////                        course.getTitle(),
////                        course.getUser().getUserName(),
////                        enrollment.getEnrolledAt().toString(), // LocalDateTime을 문자열로 변환하여 전달
////                        course.getThumbnailPath()
////                );
////
////                myEnrollmentResponseDto.add(dto);
////            }
////        }
//
//        // 수강 내역에 대한 응답을 반환합니다.
//        return ResponseEntity.status(HttpServletResponse.SC_OK).body(myEnrollmentResponseDto);
//    }
//
//
//
//}
