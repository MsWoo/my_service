package mswoo.toyproject.my_service.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.MemberJoinDto;
import mswoo.toyproject.my_service.domain.dto.SuccessResponse;
import mswoo.toyproject.my_service.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchMember() {
        return ResponseEntity.ok(memberService.searchMember());
    }

    @PostMapping
    public ResponseEntity<Object> joinMember(@RequestBody MemberJoinDto memberJoinDto) {
        return ResponseEntity.ok(memberService.joinMember(memberJoinDto));
    }

}
