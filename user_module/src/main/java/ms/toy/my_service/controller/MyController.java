package ms.toy.my_service.controller;

import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.PasswordChangeDto;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my")
@RequiredArgsConstructor
public class MyController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> getMyInfo(@AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(userService.getUserInfo(Long.valueOf(memberInfo.getMemberSeq())));
    }

    @PostMapping("/password")
    public ResponseEntity<Object> changePassword(
            @RequestBody PasswordChangeDto passwordChangeDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(userService.changePassword(passwordChangeDto, memberInfo));
    }



}
