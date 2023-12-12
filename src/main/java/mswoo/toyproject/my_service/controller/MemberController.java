package mswoo.toyproject.my_service.controller;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.MemberEditDto;
import mswoo.toyproject.my_service.domain.dto.MemberInfo;
import mswoo.toyproject.my_service.domain.dto.MemberJoinDto;
import mswoo.toyproject.my_service.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMemberInfo(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberInfo(id));
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinMember(@RequestBody MemberJoinDto memberJoinDto) {
        return ResponseEntity.ok(memberService.joinMember(memberJoinDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editMember(
            @PathVariable Long id,
            @RequestBody MemberEditDto memberEditDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(memberService.editMember(id, memberEditDto, memberInfo));
    }

}
