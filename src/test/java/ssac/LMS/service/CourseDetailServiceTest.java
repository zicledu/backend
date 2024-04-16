package ssac.LMS.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.LectureRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseDetailServiceTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LectureRepository lectureRepository;


    @Test
    void getCourseSummary() {
        Long courseId = 1L;
        System.out.println("courseId = " + courseId);
        Course course = courseRepository.findById(courseId).get();
        System.out.println("course = " + course);
        List<Lecture> lecturesByCourse = lectureRepository.findAllByCourse(course);
        System.out.println("lecturesByCourse = " + lecturesByCourse);

        Long totalDurationMinutes = lecturesByCourse.stream()
                .mapToLong(lecture -> {
                    String[] timeParts = lecture.getDurationMinutes().split(":");
                    int minutes = Integer.parseInt(timeParts[0]);
                    int seconds = Integer.parseInt(timeParts[1]);
                    return minutes * 60 + seconds; // 시간을 초로 변환하여 합산
                })
                .sum();
        String s = convertMinutesToTime(totalDurationMinutes);

        int size = lecturesByCourse.size();
        System.out.println("size = " + size);

        System.out.println("runTime = " + s);


    }
    public static String convertMinutesToTime(long totalSeconds) {
        // 시간, 분, 초 계산
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        int seconds = (int) (totalSeconds % 60);
        // 초는 여기서는 사용하지 않으므로 0으로 설정

        // 시간, 분, 초를 시간 형식으로 포맷팅하여 반환
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}