package com.realthon.on.events.controller;

import com.realthon.on.events.dto.request.LocalArtEventRequest;
import com.realthon.on.events.dto.response.LocalArtEventResponse;
import com.realthon.on.events.dto.response.PageResponse;
import com.realthon.on.events.service.LocalArtEventService;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class LocalArtEventController {

    private final LocalArtEventService service;


    // Read (detail)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.get(id)));
    }

    // Read (list with paging)
    @GetMapping
    public ResponseEntity<ResponseBody<PageResponse<LocalArtEventResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<LocalArtEventResponse> p = service.list(page, size);
        PageResponse<LocalArtEventResponse> body = new PageResponse<>(
                p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()
        );
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(body));
    }

/*

    // Create
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> create(@RequestBody LocalArtEventRequest req) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.create(req)));
    }

    // Update (partial: null 필드는 유지)
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBody<LocalArtEventResponse>> update(
            @PathVariable Long id, @RequestBody LocalArtEventRequest req
    ) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(service.update(id, req)));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

    */

}
