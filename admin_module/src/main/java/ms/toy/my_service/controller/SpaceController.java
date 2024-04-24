package ms.toy.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.ErrorResponse;
import ms.toy.my_service.domain.dto.SpaceDto;
import ms.toy.my_service.domain.dto.SpaceRequestDto;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.repository.search.SpaceSearchCondition;
import ms.toy.my_service.service.SpaceService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Space", description = "공간 관리 Rest API")
@RestController
@RequestMapping("/api/v1/space")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;

    @Operation(summary = "공간 검색", description = "공간을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = CommonPageDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping
    public ResponseEntity<Object> searchSpace(@ParameterObject @ModelAttribute SpaceSearchCondition searchCondition) {
        return ResponseEntity.ok(spaceService.searchSpace(searchCondition));
    }

    @Operation(summary = "공간 상세 조회", description = "공간을 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SpaceDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getSpaceInfo(@Parameter(description = "공간 ID") @PathVariable Long id) {
        return ResponseEntity.ok(spaceService.getSpaceInfo(id));
    }

    @Operation(summary = "공간 등록", description = "새로운 공간을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SpaceDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/save")
    public ResponseEntity<Object> saveSpace(@Parameter @Valid @RequestBody SpaceRequestDto spaceRequestDto,
            @AuthenticationPrincipal MemberInfo memberInfo
    ) {
        return ResponseEntity.ok(spaceService.saveSpace(spaceRequestDto, memberInfo));
    }

    @Operation(summary = "공간 삭제", description = "기존 공간을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SpaceDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdmin(@Parameter(description = "공간 ID") @PathVariable Long id) {
        return ResponseEntity.ok(spaceService.deleteSpace(id));
    }

    @Operation(summary = "공간 수정", description = "기존 공간 정보을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SpaceDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> editAdmin(
            @Parameter(description = "공간 ID") @PathVariable Long id,
            @Parameter @RequestBody SpaceRequestDto spaceRequestDto,
            @AuthenticationPrincipal MemberInfo memberInfo) {
        return ResponseEntity.ok(spaceService.editSpace(id, spaceRequestDto, memberInfo));
    }
}
