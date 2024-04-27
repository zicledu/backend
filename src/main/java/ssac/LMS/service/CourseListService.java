package ssac.LMS.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@Slf4j
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
        return latestCourses;
    }

    public List<Course> getCourseByBest() {
        List<Course> bestCourses = enrollmentRepository.findTop5CoursesByEnrollmentCount();
        return bestCourses;
    }

    public List<Enrollment> getMyClass(String userId) {
        Optional<User> user = userRepository.findById(userId);
        List<Enrollment> EnrollmentByUser = enrollmentRepository.findByUser(user.get());
        return EnrollmentByUser;
    }

    public Page<Course> getSearchCourse(String keyword, Pageable pageable) {
//        Page<Course> searchResult = courseRepository.findByKeyword("%" + keyword + "%", pageable);
//        log.info("searchResult={}", searchResult);
//        return searchResult;
        return courseRepository.findByKeyword("%" + keyword + "%", pageable);

    }

    public  List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 사용자의 수강 내역을 가져오는 메서드
    public boolean getEnrollment(String userId, Long courseId) {
        // 사용자 ID, 강좌 ID, 수강 ID를 기준으로 수강 내역을 조회합니다.
        User user = userRepository.findById(userId).get();
        Course course = courseRepository.findById(courseId).get();
        boolean isExist = enrollmentRepository.existsByUserAndCourse(user, course);

        return isExist;
    }

    public Course getEnrollmentCourse(Long courseId) {
        // 사용자 ID, 강좌 ID, 수강 ID를 기준으로 수강 내역을 조회합니다.
        Course course = courseRepository.findById(courseId).get();

        return course;
    }
}





