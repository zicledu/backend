package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {


    @Query("SELECT c FROM Course c " +
            "JOIN c.user u " +
            "WHERE LOWER(c.title) LIKE LOWER(concat('%', :keyword, '%')) " +
            "   OR LOWER(c.tags) LIKE LOWER(concat('%', :keyword, '%')) " +
            "   OR LOWER(u.userName) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Course> findByKeyword(@Param("keyword") String keyword);

    List<Course> findAllByUser(User user);

    Optional<Course> findById(Long id);
}