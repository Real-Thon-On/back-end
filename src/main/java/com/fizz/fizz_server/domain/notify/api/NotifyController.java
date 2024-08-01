package com.fizz.fizz_server.domain.notify.api;

import com.fizz.fizz_server.domain.notify.service.NotifyService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notify")
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        return notifyService.subscribe(userPrincipal.getUserId(), lastEventId);
    }
}

