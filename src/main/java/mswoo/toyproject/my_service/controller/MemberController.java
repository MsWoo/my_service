package mswoo.toyproject.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.ErrorResponse;
import mswoo.toyproject.my_service.domain.dto.MemberDto;
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

@Tag(name = "Member", description = "멤버 관리 Rest API")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 검색", description = "멤버를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Object> searchMember() {
        return ResponseEntity.ok(memberService.searchMember());
    }

    @Operation(summary = "멤버 상세 조회", description = "멤버를 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMemberInfo(@Parameter(description = "멤버 ID") @PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberInfo(id));
    }

    @Operation(summary = "멤버 등록", description = "새로운 멤버를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/join")
    public ResponseEntity<Object> joinMember(@Parameter @Valid @RequestBody MemberJoinDto memberJoinDto) {
        return ResponseEntity.ok(memberService.joinMember(memberJoinDto));
    }

    @Operation(summary = "멤버 삭제", description = "기존 멤버를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMember(@Parameter(description = "멤버 ID") @PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }

    @Operation(summary = "멤버 수정", description = "기존 멤버 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> editMember(
            @Parameter(description = "멤버 ID") @PathVariable Long id,
            @Parameter @RequestBody MemberEditDto memberEditDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(memberService.editMember(id, memberEditDto, memberInfo));
    }

}
