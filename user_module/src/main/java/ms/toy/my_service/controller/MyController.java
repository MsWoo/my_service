package ms.toy.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.ErrorResponse;
import ms.toy.my_service.domain.dto.PasswordChangeDto;
import ms.toy.my_service.domain.dto.UserDetailDto;
import ms.toy.my_service.domain.dto.UserDto;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MyPage", description = "MyPage Rest API")
@RestController
@RequestMapping("/api/v1/my")
@RequiredArgsConstructor
public class MyController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserDetailDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getMyInfo(@AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(userService.getUserInfo(Long.valueOf(memberInfo.getMemberSeq())));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/password")
    public ResponseEntity<Object> changePassword(
            @Parameter @RequestBody PasswordChangeDto passwordChangeDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(userService.changePassword(passwordChangeDto, memberInfo));
    }



}
