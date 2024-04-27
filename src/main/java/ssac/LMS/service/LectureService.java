package ssac.LMS.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.repository.LectureRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;

    public List<Lecture> getLecturesByCourse(Course course) {
        return lectureRepository.findAllByCourse(course);
    }


}
