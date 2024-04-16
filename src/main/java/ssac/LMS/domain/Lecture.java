package ssac.LMS.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.Duration;

@Entity
@Table(name="lectures")
@Getter
@Setter
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="lecture_id", nullable = false)
    private Long lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId")
    @JsonIgnore
    private Course course;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(name="duration_minutes", nullable = false)
    private String  durationMinutes;

    @Column(name="lecture_order", unique=false, nullable = false)
    private Integer lectureOorder;

    @Column(name="video_path_720", nullable = false)
    private String videoPath720;

    @Column(name="video_path_1080", nullable = false)
    private String videoPath1080;

    @Column(name="video_path_original", nullable = false)
    private String videoPathOriginal;

}
