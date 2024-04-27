package ssac.LMS.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name="user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String telephone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false,  columnDefinition="boolean default false")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

}
