package ssac.LMS.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Enrollment;
import ssac.LMS.domain.User;
import ssac.LMS.dto.CreateEnrollmentRequestDto;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.EnrollmentRepository;
import ssac.LMS.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    public Long save(CreateEnrollmentRequestDto createEnrollmentRequestDto) {
        String userId = createEnrollmentRequestDto.getUserId();
        Long courseId = createEnrollmentRequestDto.getCourseId();

        User user = userRepository.findById(userId).get();
        Course course = courseRepository.findById(courseId).get();

        Optional<Enrollment> existedEnrollment = enrollmentRepository.findByUserAndCourse(user, course);

        if (existedEnrollment.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Enrollment already exist");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return savedEnrollment.getEnrollmentId();
    }
}
