package com.realthon.on.events.controller;

import com.realthon.on.events.dto.response.LocalArtEventResponse;
import com.realthon.on.events.dto.response.PageResponse;
import com.realthon.on.events.service.LocalArtEventService;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Local Art Events API", description = "지역 예술 행사(LocalArtEvent) 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/api/events",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class LocalArtEventController {

    private final LocalArtEventService service;

    @Operation(
            summary = "행사 상세 조회",
            description = "ID로 지역 예술 행사 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseBody.class),
                                    examples = @ExampleObject(name = "detail-success",
                                            value = "{\n" +
                                                    "  \"success\": true,\n" +
                                                    "  \"data\": {\n" +
                                                    "    \"id\": 123,\n" +
                                                    "    \"title\": \"현대의 거울로 본 고전\",\n" +
                                                    "    \"url\": \"https://inmun360.culture.go.kr/...\",\n" +
                                                    "    \"startDate\": \"2025-06-18\",\n" +
                                                    "    \"endDate\": \"2025-08-20\",\n" +
                                                    "    \"imageUrl\": \"https://.../image/2427\",\n" +
                                                    "    \"categories\": \"['교육','인문네트워크']\",\n" +
                                                    "    \"mainCategory\": \"교육\",\n" +
                                                    "    \"siteName\": \"인문네트워크\",\n" +
                                                    "    \"siteType\": \"인문네트워크\",\n" +
                                                    "    \"scrapedDate\": \"2025-08-17\",\n" +
                                                    "    \"detailTitle\": \"인문네트워크\",\n" +
                                                    "    \"detailImages\": \"['https://.../img1','https://.../img2']\",\n" +
                                                    "    \"imageCount\": 7\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "대상 없음")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> get(
            @Parameter(description = "행사 ID", required = true, example = "123")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.get(id)));
    }

    @Operation(
            summary = "행사 목록 조회(페이지)",
            description = "지역 예술 행사 목록을 페이지네이션으로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseBody.class),
                                    examples = @ExampleObject(name = "list-success",
                                            value = "{\n" +
                                                    "  \"success\": true,\n" +
                                                    "  \"data\": {\n" +
                                                    "    \"content\": [\n" +
                                                    "      {\n" +
                                                    "        \"id\": 123,\n" +
                                                    "        \"title\": \"현대의 거울로 본 고전\",\n" +
                                                    "        \"url\": \"https://inmun360.culture.go.kr/...\",\n" +
                                                    "        \"startDate\": \"2025-06-18\",\n" +
                                                    "        \"endDate\": \"2025-08-20\"\n" +
                                                    "      }\n" +
                                                    "    ],\n" +
                                                    "    \"page\": 0,\n" +
                                                    "    \"size\": 20,\n" +
                                                    "    \"totalElements\": 56,\n" +
                                                    "    \"totalPages\": 3\n" +
                                                    "  }\n" +
                                                    "}"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ResponseBody<PageResponse<LocalArtEventResponse>>> list(
            @Parameter(description = "페이지 번호(0-base)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<LocalArtEventResponse> p = service.list(page, size);
        PageResponse<LocalArtEventResponse> body = new PageResponse<>(
                p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()
        );
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(body));
    }

    /*
    // (필요 시 주석 해제 후 문서화 추가)

    @Operation(summary = "행사 생성", description = "새 지역 예술 행사 레코드를 생성합니다.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성 요청 본문",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LocalArtEventRequest.class))
            )
            @RequestBody LocalArtEventRequest req
    ) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.create(req)));
    }

    @Operation(summary = "행사 부분 수정", description = "null 필드는 유지, 값이 있는 필드만 수정합니다.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> update(
            @Parameter(description = "행사 ID", required = true, example = "123")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정 요청 본문",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LocalArtEventRequest.class))
            )
            @RequestBody LocalArtEventRequest req
    ) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.update(id, req)));
    }

    @Operation(summary = "행사 삭제", description = "ID로 지역 예술 행사 레코드를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Void>> delete(
            @Parameter(description = "행사 ID", required = true, example = "123")
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }
    */
}
