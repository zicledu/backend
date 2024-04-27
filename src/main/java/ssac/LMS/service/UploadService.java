package ssac.LMS.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssac.LMS.domain.Course;
import ssac.LMS.domain.Lecture;
import ssac.LMS.domain.Tags;
import ssac.LMS.domain.User;
import ssac.LMS.dto.UploadRequestDto;
import ssac.LMS.repository.CourseRepository;
import ssac.LMS.repository.LectureRepository;
import ssac.LMS.repository.UserRepository;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;

    private AmazonS3 getCredentials() {
        log.info(accessKey);
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
        return amazonS3Client;
    }

    public String uploadImage(MultipartFile file, String email) throws IOException {

        log.info("uploadImage={}", file.getOriginalFilename());

        AmazonS3 credentials = getCredentials();

        UUID uuid = UUID.randomUUID();
        String subString = getSubString(file);

        String name = email + '/' + uuid + subString;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        credentials.putObject(new PutObjectRequest("lmsh-test", name, file.getInputStream(), objectMetadata));

        return credentials.getUrl("lmsh-test", name).toString();
    }

    public String uploadMarkdown(String markdown, String email) {
        AmazonS3 credentials = getCredentials();

        UUID uuid = UUID.randomUUID();
        String fileName = "markdown/" + email + '/' + uuid + ".md";

        log.info("uploadMarkdown={}", fileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(markdown.getBytes().length);


        credentials.putObject(
                new PutObjectRequest("fronttest-min", fileName, new ByteArrayInputStream(markdown.getBytes()), objectMetadata)
        );

        String getUrl = credentials.getUrl("fronttest-min", fileName).toString().replace("%40", "@");
        log.info("getUrl={}", getUrl);
        return getUrl;
    }

    public Map<String,String> uploadThumbnail(MultipartFile thumbnail, String email) throws IOException {
        AmazonS3 credentials = getCredentials();
        UUID uuid = UUID.randomUUID();

        String subString = getSubString(thumbnail);
        String fileName = (email + "/" + uuid + subString).replace(" ", "-");

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(thumbnail.getSize());

        credentials.putObject(
                new PutObjectRequest("image-input-748596", fileName, thumbnail.getInputStream(), objectMetadata)
        );

        String baseUrl = "https://lms-outputvod.s3.ap-northeast-2.amazonaws.com/";
        String thumbnailPath1340x958 = (baseUrl + email + "/thumbnail/" + "1340x958/" + "resized-" + uuid + "-1340x958" + ".png").replace(" ", "-").replace("\\@", "\\%40");
        String thumbnailPath450x250 = (baseUrl + email + "/thumbnail/" + "450x250/" + "resized-" + uuid + "-450x250" + ".png").replace(" ", "-").replace("\\@", "\\%40");


        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("thumbnailPath1340x958", thumbnailPath1340x958);
        urlMap.put("thumbnailPath450x250", thumbnailPath450x250);
        urlMap.put("thumbnailPathOriginal", "https://image-input-748596.s3.ap-northeast-2.amazonaws.com/"+fileName);
        return urlMap;
    }

    public void uploadVideo(List<Map<String, Object>> videos, String email, Long courseId) throws IOException, JCodecException {

        AmazonS3 credentials = getCredentials();
        Course course = courseRepository.findById(courseId).get();

        for (Map<String, Object> video : videos) {

            UUID uuid = UUID.randomUUID();

            MultipartFile file =(MultipartFile) video.get("file");

            String duration = getDuration(file);
            log.info("duration={}", duration);

            String subString = getSubString(file);

            String fileName = (email + "/" + uuid + subString).replace(" ", "-");

            ObjectMetadata objectMetadata = new ObjectMetadata();

            log.info("fileNameVideo={}", fileName);
            credentials.putObject(
                    new PutObjectRequest("lms-inputvod-968574",fileName, file.getInputStream(), objectMetadata)
            );

            Lecture lecture = new Lecture();
            lecture.setLectureOorder(Integer.parseInt(video.get("order").toString()));
            lecture.setTitle(video.get("title").toString());
            lecture.setCourse(course);
            lecture.setDurationMinutes(duration);

            String path720 = String.format("https://lms-outputvod.s3.ap-northeast-2.amazonaws.com/%s/%s/%s_720p.m3u8", email, uuid, uuid).replace("\\@", "\\%40");
            String path1080 = String.format("https://lms-outputvod.s3.ap-northeast-2.amazonaws.com/%s/%s/%s_1080p.m3u8", email, uuid, uuid).replace("\\@", "\\%40");
            String original = String.format("https://lms-outputvod.s3.ap-northeast-2.amazonaws.com/%s/%s/%s.m3u8", email, uuid, uuid).replace("\\@", "\\%40");
            lecture.setVideoPath720(path720);
            lecture.setVideoPath1080(path1080);
            lecture.setVideoPathOriginal(original);

            lectureRepository.save(lecture);

        }
    }

    private static String getSubString(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int lastIndex = originalFilename.lastIndexOf(".");
        String subString = originalFilename.substring(lastIndex);
        return subString;
    }

    private static String getDuration(MultipartFile file) throws IOException, JCodecException {
        File file1 = new File(file.getOriginalFilename());
        FileUtils.writeByteArrayToFile(file1, file.getBytes());
        FrameGrab frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file1));
        double totalDuration = frameGrab.getVideoTrack().getMeta().getTotalDuration();
        int minutes = (int) (totalDuration / 60);
        int seconds = (int) (totalDuration % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public Long saveCourse(Jwt jwt, Map<String, String> thumbnailPath, String markdownPath, UploadRequestDto uploadRequestDto) {

        // user 가져오기
        String username = jwt.getClaim("cognito:username").toString();
        log.info("username={}", username);
        User user = userRepository.findById(username).get();

        // Course 저장하기
        String title = uploadRequestDto.getTitle();

        String selectedDate = uploadRequestDto.getSelectedDate();
        LocalDateTime dateTimeString =  parseDateTimeString(selectedDate);

        int price = uploadRequestDto.getPrice();
        String thumbnailPath1340x958 = thumbnailPath.get("thumbnailPath1340x958");
        String thumbnailPath450x250 = thumbnailPath.get("thumbnailPath450x250");
        String thumbnailPathOriginal = thumbnailPath.get("thumbnailPathOriginal");

        log.info("thumbnailPath1340x958={}", thumbnailPath1340x958);

        Tags django = Tags.DJANGO;

        Course course = new Course();
        course.setPrice(price);
        course.setTags(django);
        course.setDescription(markdownPath);
        course.setTitle(title);
        course.setStartedAt(dateTimeString);
        course.setThumbnailPath1340(thumbnailPath1340x958);
        course.setThumbnailPath450(thumbnailPath450x250);
        course.setThumbnailPath(thumbnailPathOriginal);
        course.setUser(user);

        Course savedCourse = courseRepository.save(course);
        Long courseId = savedCourse.getCourseId();
        return courseId;
    }
    private static LocalDateTime parseDateTimeString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

}
