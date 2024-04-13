package ssac.LMS.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ssac.LMS.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.EnrollmentRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseListService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private EntityManager entityManager;


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
}

