package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssac.LMS.domain.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {


}
