package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssac.LMS.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
