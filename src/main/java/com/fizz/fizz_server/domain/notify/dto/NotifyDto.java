package com.fizz.fizz_server.domain.notify.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fizz.fizz_server.domain.notify.domain.NotificationType;
import com.fizz.fizz_server.domain.notify.domain.Notify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotifyDto {
    String id;
    String nickname;
    String content;
    Boolean isRead;
    NotificationType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt;
    String videoUrl;
    String thumbnailUrl;

    public static NotifyDto createResponse(Notify notify) {
        return NotifyDto.builder()
                .id(notify.getId().toString())
                .nickname(notify.getReceiver().getNickname())
                .content(notify.getContent())
                .isRead(notify.getIsRead())
                .type(notify.getNotificationType())
                .createdAt(notify.getCreatedAt())
                .videoUrl(notify.getVideoUrl())
                .thumbnailUrl(notify.getThumbnailUrl())
                .build();

    }
}
