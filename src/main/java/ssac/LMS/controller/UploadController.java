package ssac.LMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssac.LMS.dto.ImageResponseDto;
import ssac.LMS.dto.MarkdownResponseDto;
import ssac.LMS.dto.UploadRequestDto;
import ssac.LMS.dto.UploadResponseDto;
import ssac.LMS.service.UploadService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> upload(UploadRequestDto uploadRequestDto, @AuthenticationPrincipal Jwt jwt) throws IOException {
        log.info("jwtUserEmail={}", jwt.getClaim("email").toString());

        String email = jwt.getClaim("email").toString();

        Map<String, String> thumbnailPath = uploadService.uploadThumbnail(uploadRequestDto.getThumbnail(), email);
        String markdownPath = uploadService.uploadMarkdown(uploadRequestDto.getMarkdown(), email);

        Long courseId = uploadService.saveCourse(jwt, thumbnailPath, markdownPath, uploadRequestDto);

        uploadService.uploadVideo(uploadRequestDto.getLecture(), email, courseId);

        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(new UploadResponseDto(courseId));
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) throws IOException {

        String email = jwt.getClaim("email").toString();

        String url = uploadService.uploadImage(file, email);
        return ResponseEntity.status(HttpServletResponse.SC_OK).body(new ImageResponseDto(url));
    }


}
