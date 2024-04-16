package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssac.LMS.domain.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
