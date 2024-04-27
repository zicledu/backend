package ssac.LMS.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "04f8edec-00d1-7011-33c0-423a3946fd88") // 사용자 인증 설정
    public void testGetMyEnrollment() throws Exception {
        String userId = "04f8edec-00d1-7011-33c0-423a3946fd88";

        MvcResult result = mockMvc.perform(get("/enrollment/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK를 기대
                .andReturn();

        // 테스트 결과 출력
        System.out.println("Response Body: " + result.getResponse().getContentAsString());
    }
}
