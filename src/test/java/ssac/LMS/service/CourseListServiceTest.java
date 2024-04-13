package ssac.LMS.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ssac.LMS.domain.Course;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.EnrollmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CourseListServiceTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getCourseByNew() {
        Sort sort = Sort.by(Sort.Direction.DESC, "startedAt");

        Pageable pageable = PageRequest.of(0, 5, sort);

        // 최신순으로 정렬된 최신 강의 목록을 가져옵니다.
        List<Course> content = courseRepository.findAll(pageable).toList();
        for (Course course : content) {
            System.out.println("courseName = " + course.getTitle()); // course의 이름 가져오기
            System.out.println("courseDescription = " + course.getDescription());
            System.out.println("courseStartedAt = " + course.getStartedAt());
            System.out.println("coursePrice = " + course.getPrice());
            System.out.println("courseTags = " + course.getTags());
            System.out.println("\n");
        }


    }

    @Test
    void getCourseByBest() {
        Query query = entityManager.createQuery(
                "SELECT e.course.courseId, COUNT(e) AS enrollmentCount " +
                        "FROM Enrollment e " +
                        "GROUP BY e.course.courseId " +
                        "ORDER BY enrollmentCount DESC"
        );
        query.setMaxResults(5); // 상위 5개만 선택

        List<Object[]> resultList = query.getResultList();
        List<Course> bestCourses = new ArrayList<>();

        for (Object[] result : resultList) {
            Long courseId = (Long) result[0];
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course != null) {
                bestCourses.add(course);
            }
        }

        assertFalse(bestCourses.isEmpty(), "가장 많이 등록된 강의 리스트가 비어 있습니다.");
        assertTrue(bestCourses.size() <= 5, "가장 많이 등록된 강의 리스트 크기가 5를 초과합니다.");

        for (Course course : bestCourses) {
            System.out.println("courseId: " + course.getCourseId());
            System.out.println("courseName = " + course.getTitle()); // course의 이름 가져오기
            System.out.println("courseDescription = " + course.getDescription());
            System.out.println("courseStartedAt = " + course.getStartedAt());
            System.out.println("coursePrice = " + course.getPrice());
            System.out.println("courseTags = " + course.getTags());
            System.out.println("\n");
        }
    }

}