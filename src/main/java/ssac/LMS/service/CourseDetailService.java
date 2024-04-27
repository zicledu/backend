package ssac.LMS.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.domain.User;
import ssac.LMS.dto.CourseDetailCurriculumResponseDto;
import ssac.LMS.dto.CourseDetailSummaryResponseDto;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.LectureRepository;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseDetailService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    public CourseDetailSummaryResponseDto getCourseSummary(Long courseId) {
        log.info("getCourseSummary.courseId={}", courseId);

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
        int price = course.getPrice();
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(price);

        CourseDetailSummaryResponseDto courseDetailSummaryResponseDto = new CourseDetailSummaryResponseDto(course.getCourseId(), course.getTitle(), course.getUser().getUserName(), size, totalRunTime, course.getThumbnailPath1340(), formattedPrice, course.getTags());
        return courseDetailSummaryResponseDto;
    }

    public List<Lecture> getCourseCurriculum(Long courseId) {
        log.info("getCourseCurriculum.courseId={}", courseId);
        Course course = courseRepository.findById(courseId).get();
        List<Lecture> lectures = lectureRepository.findAllByCourse(course);
        return lectures;
    }


    public String getInfo(Long courseId) {
        log.info("getInfo.courseId={}", courseId);
        Course course = courseRepository.findById(courseId).get();
        String markdownUrl = course.getDescription();
        // HTTP GET 요청을 보내서 마크다운 내용 가져오기
        RestTemplate restTemplate = new RestTemplate();
        String markdown = restTemplate.getForObject(markdownUrl, String.class);
        return markdown;
    }
    public HashMap<String, Object> getInstructor(Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        User user = course.getUser();
        List<Course> courses = courseRepository.findAllByUser(user);
        int count = courses.size();
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("count", count);
        return result;
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
