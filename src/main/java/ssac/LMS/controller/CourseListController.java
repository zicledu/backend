package ssac.LMS.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssac.LMS.domain.Course;
import ssac.LMS.dto.CourseResponseDto;
import ssac.LMS.service.CourseListService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@Slf4j
public class CourseListController {
    private final CourseListService courseListService;

    @Data
    static class Result<T> {
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }
    }
    @GetMapping("/new")
    public ResponseEntity<Result> getCourseByNew() {
        log.info("new");
        List<Course> latestCourses = courseListService.getLatestCourses();

        List<CourseResponseDto> courseResponseDtoStream = latestCourses.stream()
                .map(m -> new CourseResponseDto(m.getTitle(), m.getDescription(), m.getStartedAt(), m.getPrice(),
                        m.getTags(), m.getThumbnailPath()))
                        .collect(Collectors.toList());
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new Result(courseResponseDtoStream.size(), courseResponseDtoStream));
    }

    @GetMapping("/best")
    public ResponseEntity<Result> getCourseByBest() {

        log.info("best");
        List<Course> bestCourses = courseListService.getCourseByBest();

        List<CourseResponseDto> courseResponseDtoStream = bestCourses.stream()
                .map(m -> new CourseResponseDto(m.getTitle(), m.getDescription(), m.getStartedAt(), m.getPrice(),
                        m.getTags(), m.getThumbnailPath()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new Result(courseResponseDtoStream.size(), courseResponseDtoStream));

    }
}

