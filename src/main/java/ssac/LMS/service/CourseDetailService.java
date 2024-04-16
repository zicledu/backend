package ssac.LMS.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.domain.User;
import ssac.LMS.dto.CourseDetailCurriculumResponseDto;
import ssac.LMS.dto.CourseDetailSummaryResponseDto;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.LectureRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseDetailService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    public CourseDetailSummaryResponseDto getCourseSummary(Long courseId) {

        Course course = courseRepository.findById(courseId).get();

        List<Lecture> lecturesByCourse = lectureRepository.findAllByCourse(course);

        Long totalDurationMinutes = lecturesByCourse.stream()
                .mapToLong(lecture -> {
                    String[] timeParts = lecture.getDurationMinutes().split(":");
                    int minutes = Integer.parseInt(timeParts[0]);
                    int seconds = Integer.parseInt(timeParts[1]);
                    return minutes * 60 + seconds; // 시간을 초로 변환하여 합산
                })
                .sum();

        String totalRunTime = convertMinutesToTime(totalDurationMinutes);

        int size = lecturesByCourse.size();

        CourseDetailSummaryResponseDto courseDetailSummaryResponseDto = new CourseDetailSummaryResponseDto(course.getCourseId(), course.getTitle(), course.getUser().getUserName(), size, totalRunTime, course.getThumbnailPath(), course.getPrice(), course.getTags());
        return courseDetailSummaryResponseDto;
    }

    public List<Lecture> getCourseCurriculum(Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        List<Lecture> lectures = lectureRepository.findAllByCourse(course);
        return lectures;
    }

    public User getInstructor(Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        User user = course.getUser();
        return user;
    }

    private static String convertMinutesToTime(long totalSeconds) {
        // 시간, 분, 초 계산
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        int seconds = (int) (totalSeconds % 60);
        // 초는 여기서는 사용하지 않으므로 0으로 설정

        // 시간, 분, 초를 시간 형식으로 포맷팅하여 반환
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
