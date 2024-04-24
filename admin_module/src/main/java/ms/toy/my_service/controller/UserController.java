package ms.toy.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.*;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.repository.search.UserSearchCondition;
import ms.toy.my_service.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "이용자 관리 Rest API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "이용자 검색", description = "이용자를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = CommonPageDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> searchUser(@ParameterObject @ModelAttribute UserSearchCondition searchCondition) {
        return ResponseEntity.ok(userService.searchUser(searchCondition));
    }

    @Operation(summary = "이용자 상세 조회", description = "이용자를 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserInfo(@Parameter(description = "이용자 ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @Operation(summary = "이용자 삭제", description = "기존 이용자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@Parameter(description = "이용자 ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @Operation(summary = "이용자 수정", description = "기존 이용자 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> editUser(
            @Parameter(description = "이용자 ID") @PathVariable Long id,
            @Parameter @RequestBody UserEditDto userEditDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(userService.editUser(id, userEditDto, memberInfo));
    }

}
