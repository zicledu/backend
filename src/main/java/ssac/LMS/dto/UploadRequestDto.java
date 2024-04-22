package ssac.LMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDto {

    private String title;
    private MultipartFile thumbnail;
    private int price;
    private String markdown;
    private String selectedDate;
    private List<Map<String, Object>> lecture;

}
