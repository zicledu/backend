-- data.sql

-- users 테이블에 테스트 데이터 삽입
INSERT INTO users (is_deleted, created_at,user_id, email, role, telephone, user_name)
VALUES
    (false, '2024-04-09 14:30:00', '101','aji7day@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '김민아'),
    (false, '2024-04-09 14:30:00', '102','yhnmko12@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '김정모'),
    (false, '2024-04-09 14:30:00', '74a89d3c-60a1-707d-5f42-d51b16e219b7','tkdgns5817@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '박상훈'),
    (false, '2024-04-09 14:30:00', '104','1472MOMENT@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '박예린'),
    (false, '2024-04-09 14:30:00', '04f8edec-00d1-7011-33c0-423a3946fd88','6dreamhigh@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '황서정'),
    (false, '2024-04-09 14:30:00', '106', 'someone@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍길동'),
    (false, '2024-04-09 14:30:00', '107', 'someone2@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍이'),
    (false, '2024-04-09 14:30:00', '108', 'someone3@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍삼'),
    (false, '2024-04-09 14:30:00', '109', 'someone4@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍사동'),
    (false, '2024-04-09 14:30:00', '110', 'another5@gmail.com', 'ROLE_ADMIN', '010-1111-1111', '관리자');
-- courses 테이블에 테스트 데이터 삽입
INSERT INTO courses
(course_id, user_id, title, description, started_at, end_at, price, tags, thumbnail_path, thumbnail_path_1340, thumbnail_path_450)
VALUES
    (100, '106', '100번 Course', 'Learn Java programming', '2024-04-01 09:00:00', '2035-06-30 18:00:00', 20000, 'JAVA', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%981.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%981.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%981.PNG'),
    (200, '107', '200번 Course', 'Learn Python Course', '2024-04-03 10:00:00', '2035-06-30 18:00:00', 18000, 'PYTHON', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%985.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%985.PNG','https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%985.PNG'),
    (300, '108', '300번 Course', 'Learn Front-End Course', '2024-04-02 09:00:00', '2035-06-30 18:00:00', 17000, 'Front_End', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%986.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%986.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%986.PNG'),
    (400, '109', '400번 Course', 'Learn Back-End Course', '2024-04-05 10:00:00', '2035-06-30 18:00:00', 16000, 'Back_End', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%987.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%987.PNG', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%987.PNG');

-- enrollments 테이블에 테스트 데이터 삽입
INSERT INTO enrollments (course_id, enrolled_at, enrollment_id, user_id)
VALUES
    (100, '2024-04-09 14:00:00',100,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (200, '2024-04-08 14:10:00',200,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (300, '2024-04-10 14:20:00',300,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (400, '2024-04-07 14:30:00',400,'04f8edec-00d1-7011-33c0-423a3946fd88'),
    (100, '2024-04-09 14:00:00',500,'04f8edec-00d1-7011-33c0-423a3946fd88');

-- lecture 테이블에 테스트 데이터 삽입
INSERT INTO lectures (lecture_id, course_id, title, duration_minutes, lecture_order, video_path_720, video_path_1080, video_path_original)
VALUES
    (100, 100, 'Java Programming Course', '60:00', 1, '/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (200, 100, 'Variables and Data Types', '45:00', 2, '/videos/variables_720p.mp4', '/videos/variables_1080p.mp4', '/videos/variables_original.mp4'),
    (300, 100, 'Structures', '50:00', 3, '/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (400, 200, 'Python Programming Course', '60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (500, 200, 'Data Science Course', '60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (600, 300, 'React.js Course', '60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (700, 300, 'Vue.js Course', '60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (800, 400, 'Django Course','60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (900, 400, 'Spring Course','60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (1000, 400, 'Spring Boot Course', '60:00', 3,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4');