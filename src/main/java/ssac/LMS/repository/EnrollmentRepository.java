package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Enrollment;
import ssac.LMS.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,  Long> {
    @Query("SELECT e.course " +
            "FROM Enrollment e " +
            "GROUP BY e.course " +
            "ORDER BY COUNT(e) DESC")
    List<Course> findTop5CoursesByEnrollmentCount();

    List<Enrollment> findByUser(User user);

    Optional<Enrollment> findByUserAndCourse(User user, Course course);

    boolean existsByUserAndCourse(User user, Course course);
}
