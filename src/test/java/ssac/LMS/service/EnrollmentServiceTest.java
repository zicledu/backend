package ssac.LMS.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import ssac.LMS.controller.EnrollmentController;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Enrollment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EnrollmentServiceTest {
    @InjectMocks
    private EnrollmentController enrollmentController;


    @Mock
    private CourseListService courseListService;

    @Test
    public class EnrollmentServiceTest {

        @InjectMocks
        private EnrollmentController enrollmentController;

        @Mock
        private CourseListService courseListService;

        @Test
        public void testGetMyClass() {
            // 테스트할 userId와 jwtUserId
            String userId = "74a89d3c-60a1-707d-5f42-d51b16e219b7";
            String jwtUserId = "testJwtUserId";

            // Course 객체를 생성하여 리스트로 만듭니다.
            Course course1 = new Course(1L, "Java Course");
            Course course2 = new Course(2L, "Python Course");

            // Enrollment 객체를 생성하여 리스트로 만듭니다.
            Enrollment enrollment1 = new Enrollment(1L, new Date(2024-04-09 14:00:00), course1, userId);
            Enrollment enrollment2 = new Enrollment(2L, new Date(2024-04-09 14:00:00), course2, userId);
            List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);

            // courseListService.getMyClass(userId)가 호출될 때 반환할 값을 설정합니다.
            when(courseListService.getMyClass(userId)).thenReturn(enrollments);

            // Jwt 객체를 생성하여 만듭니다.
            Jwt jwt = mock(Jwt.class);
            when(jwt.getClaim("cognito:username")).thenReturn(jwtUserId);

            // getMyClass 메서드를 호출합니다.
            ResponseEntity<?> responseEntity = enrollmentController.getMyClass(userId, jwt);

            // 반환된 ResponseEntity가 예상대로 되었는지 확인합니다.
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            // courseListService.getMyClass(userId)가 제대로 호출되었는지 확인합니다.
            verify(courseListService).getMyClass(userId);
        }
    }