package ssac.LMS.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
public class MainController {

    @GetMapping("/main")
    public String adminP(@AuthenticationPrincipal Jwt jwt) {

        return "Main Controller";
    }
}
