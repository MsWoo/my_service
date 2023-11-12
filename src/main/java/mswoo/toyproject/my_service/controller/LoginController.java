package mswoo.toyproject.my_service.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.LoginDto;
import mswoo.toyproject.my_service.domain.dto.SuccessResponse;
import mswoo.toyproject.my_service.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        return ResponseEntity.ok(
                SuccessResponse.builder()
                        .result(true)
                        .code(String.valueOf(HttpServletResponse.SC_OK))
                        .data(loginService.login(loginDto, response))
                        .message("Success")
                        .build()
        );
    }

}
