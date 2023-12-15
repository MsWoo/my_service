package mswoo.toyproject.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.LoginDto;
import mswoo.toyproject.my_service.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "로그인 Rest API")
@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping
    public ResponseEntity login(
            @Parameter @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response) {
        return ResponseEntity.ok(loginService.login(loginDto, response));
    }

}
