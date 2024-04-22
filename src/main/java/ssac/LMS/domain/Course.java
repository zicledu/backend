package ssac.LMS.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name="started_at", nullable = false)
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name="end_at", nullable = true)
    private LocalDateTime endAt;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tags tags;

    @Column(name="thumbnail_path", nullable = false)
    private String thumbnailPath;

    @Column(name="thumbnail_path_1340", nullable = false)
    private String thumbnailPath1340;

    @Column(name="thumbnail_path_450", nullable = false)
    private String thumbnailPath450;



    @OneToMany(mappedBy = "course", fetch=FetchType.LAZY)
    private List<Enrollment> enrollment;



}
