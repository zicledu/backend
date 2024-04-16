package ssac.LMS.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ssac.LMS.domain.Course;
import org.springframework.stereotype.Service;
import ssac.LMS.domain.Enrollment;
import ssac.LMS.domain.User;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.EnrollmentRepository;
import ssac.LMS.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CourseListService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public List<Course> getLatestCourses() {
        Sort sort = Sort.by(Sort.Direction.DESC, "startedAt");
        Pageable pageable = PageRequest.of(0, 5, sort);

        // 최신순으로 정렬된 최신 강의 목록을 가져옵니다.
        List<Course> latestCourses = courseRepository.findAll(pageable).getContent();

        for (Course course : latestCourses) {
            System.out.println("courseName = " + course.getTitle()); // course의 이름 가져오기
            System.out.println("courseName = " + course.getDescription());
            System.out.println("courseName = " + course.getStartedAt());
            System.out.println("courseName = " + course.getPrice());
            System.out.println("courseName = " + course.getTags());
            System.out.println("\n");
        }

        return latestCourses;
    }

    public List<Course> getCourseByBest() {
        List<Course> bestCourses = enrollmentRepository.findTop5CoursesByEnrollmentCount();

        for (Course course : bestCourses) {
            System.out.println("courseId: " + course.getCourseId());
            System.out.println("courseName = " + course.getTitle());
            System.out.println("courseDescription = " + course.getDescription());
            System.out.println("courseStartedAt = " + course.getStartedAt());
            System.out.println("coursePrice = " + course.getPrice());
            System.out.println("courseTags = " + course.getTags());
            System.out.println("\n");
        }
        return bestCourses;
    }

    public List<Enrollment> getMyClass(String userId) {
        Optional<User> user = userRepository.findById(userId);
        System.out.println("user = " + user);
        List<Enrollment> EnrollmentByUser = enrollmentRepository.findByUser(user.get());
        return EnrollmentByUser;
    }
}

