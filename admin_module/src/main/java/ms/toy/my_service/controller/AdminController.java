package ms.toy.my_service.controller;

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
import ms.toy.my_service.domain.dto.AdminDto;
import ms.toy.my_service.domain.dto.AdminEditDto;
import ms.toy.my_service.domain.dto.AdminJoinDto;
import ms.toy.my_service.domain.dto.ErrorResponse;
import ms.toy.my_service.domain.dto.UserInfo;
import ms.toy.my_service.service.AdminService;
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

@Tag(name = "Admin", description = "관리자 관리 Rest API")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 검색", description = "관리자를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Object> searchAdmin() {
        return ResponseEntity.ok(adminService.searchAdmin());
    }

    @Operation(summary = "관리자 상세 조회", description = "관리자를 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = AdminDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAdminInfo(@Parameter(description = "관리자 ID") @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminInfo(id));
    }

    @Operation(summary = "관리자 등록", description = "새로운 관리자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = AdminDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/join")
    public ResponseEntity<Object> joinAdmin(@Parameter @Valid @RequestBody AdminJoinDto adminJoinDto) {
        return ResponseEntity.ok(adminService.joinAdmin(adminJoinDto));
    }

    @Operation(summary = "관리자 삭제", description = "기존 관리자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = AdminDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdmin(@Parameter(description = "관리자 ID") @PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteAdmin(id));
    }

    @Operation(summary = "관리자 수정", description = "기존 관리자 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = AdminDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> editAdmin(
            @Parameter(description = "관리자 ID") @PathVariable Long id,
            @Parameter @RequestBody AdminEditDto adminEditDto,
            @AuthenticationPrincipal UserInfo userInfo) {
        return ResponseEntity.ok(adminService.editAdmin(id, adminEditDto, userInfo));
    }

}
