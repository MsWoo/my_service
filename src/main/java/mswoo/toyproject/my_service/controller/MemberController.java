package mswoo.toyproject.my_service.controller;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.MemberJoinDto;
import mswoo.toyproject.my_service.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Object> join(@RequestBody MemberJoinDto memberJoinDto) {
        return ResponseEntity.ok(memberService.joinMember(memberJoinDto));
    }

}
