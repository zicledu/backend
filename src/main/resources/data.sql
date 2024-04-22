-- data.sql

-- users 테이블에 테스트 데이터 삽입
INSERT INTO users (is_deleted, created_at,user_id, email, role, telephone, user_name)
VALUES
    (false, '2024-04-09 14:30:00', '101','aji7day@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '김민아'),
    (false, '2024-04-09 14:30:00', '102','yhnmko12@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '김정모'),
    (false, '2024-04-09 14:30:00', '74a89d3c-60a1-707d-5f42-d51b16e219b7','tkdgns5817@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '박상훈'),
    (false, '2024-04-09 14:30:00', '104','1472MOMENT@gmail.com', 'ROLE_STUDENT', '010-1111-1111', '박예린'),
    (false, '2024-04-09 14:30:00', '105','6dreamhigh@naver.com', 'ROLE_STUDENT', '010-1111-1111', '황서정'),
    (false, '2024-04-09 14:30:00', '106', 'someone@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍길동'),
    (false, '2024-04-09 14:30:00', '107', 'someone2@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍이'),
    (false, '2024-04-09 14:30:00', '108', 'someone3@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍삼'),
    (false, '2024-04-09 14:30:00', '109', 'someone4@gmail.com', 'ROLE_INSTRUCTOR', '010-1111-1111', '홍사동'),
    (false, '2024-04-09 14:30:00', '110', 'another5@gmail.com', 'ROLE_ADMIN', '010-1111-1111', '관리자');
-- courses 테이블에 테스트 데이터 삽입
INSERT INTO courses
(course_id, user_id, title, description, started_at, end_at, price, tags, thumnail_path)
VALUES
    (1, '106', 'Java Course', 'Learn Java programming', '2024-04-01 09:00:00', '2035-06-30 18:00:00', 20000, 'JAVA', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%981.PNG'),
    (2, '107', 'Python Course', 'Learn Python Course', '2024-04-03 10:00:00', '2035-06-30 18:00:00', 18000, 'PYTHON', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%985.PNG'),
    (3, '108', 'Front-End Course', 'Learn Front-End Course', '2024-04-02 09:00:00', '2035-06-30 18:00:00', 17000, 'Front_End', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%986.PNG'),
    (4, '109', 'Back-End Course', 'Learn Back-End Course', '2024-04-05 10:00:00', '2035-06-30 18:00:00', 16000, 'Back_End', 'https://lmstestt.s3.amazonaws.com/video_original/aws/%EC%BA%A1%EC%B2%987.PNG');

-- enrollments 테이블에 테스트 데이터 삽입
INSERT INTO enrollments (course_id, enrolled_at, enrollment_id, user_id)
VALUES
    (1, '2024-04-09 14:00:00',1,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (2, '2024-04-08 14:10:00',2,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (3, '2024-04-10 14:20:00',3,'74a89d3c-60a1-707d-5f42-d51b16e219b7'),
    (4, '2024-04-07 14:30:00',4,'104'),
    (4, '2024-04-09 14:00:00',5,'105');

-- lecture 테이블에 테스트 데이터 삽입
INSERT INTO lectures (lecture_id, course_id, title, content, duration_minutes, lecture_order, video_path_720, video_path_1080, video_path_original)
VALUES
    (1, 1, 'Java Programming Course', 'Learn Java programming from scratch', '60:00', 1, '/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (2, 1, 'Variables and Data Types', 'This lecture covers variables and data types in programming languages.', '45:00', 2, '/videos/variables_720p.mp4', '/videos/variables_1080p.mp4', '/videos/variables_original.mp4'),
    (3, 1, 'Structures', 'This lecture covers control structures such as if statements and loops.', '50:00', 3, '/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (4, 2, 'Python Programming Course', 'Become proficient in Python programming language', '60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (5, 2, 'Data Science Course', 'Master data science skills with Python and machine learning', '60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (6, 3, 'React.js Course', 'Learn React.js framework for building interactive user interfaces', '60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (7, 3, 'Vue.js Course', 'Learn Vue.js for building interactive user interfaces', '60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (8, 4, 'Django Course', 'Master Django framework for building web applications with Pytho', '60:00', 1,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (9, 4, 'Spring Course', 'Master Spring Course', '60:00', 2,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4'),
    (10, 4, 'Spring Boot Course', 'Master Spring Boot Course', '60:00', 3,'/videos/control_720p.mp4', '/videos/control_1080p.mp4', '/videos/control_original.mp4');