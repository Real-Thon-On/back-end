package com.fizz.fizz_server.domain.notify.service;

import com.fizz.fizz_server.domain.notify.domain.MediaConvertMessage;
import com.fizz.fizz_server.domain.notify.domain.NotificationType;
import com.fizz.fizz_server.domain.notify.domain.Notify;
import com.fizz.fizz_server.domain.notify.dto.NotifyDto;
import com.fizz.fizz_server.domain.notify.repository.EmitterRepository;
import com.fizz.fizz_server.domain.notify.repository.NotifyRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    public static final String FILE_ENCODING_EVENT = "file-encoding-event";

    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        String emitterId = makeTimeIncludeId(user.getEmail());
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(user.getEmail());
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + user.getEmail() + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            log.info("event re-send");
            sendLostData(lastEventId, user.getEmail(), emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String email) {
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(FILE_ENCODING_EVENT)
                    .data(data)
            );
            log.info("알림을 전송함. eventId : {}, data : {}", eventId, data);
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            log.error("알림 전송에 실패했습니다. message : {}", exception.getMessage());
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String email, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(email);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> {
                    sendNotification(emitter, entry.getKey(), emitterId, entry.getValue());
                });
    }

    @Transactional
    public void send(MediaConvertMessage mediaConvertMessage, NotificationType notificationType, String content) {
        User receiver = userRepository.findById(mediaConvertMessage.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));
        Notify notification = notifyRepository.save(createNotification(receiver, notificationType, content, mediaConvertMessage));

        String receiverEmail = receiver.getEmail();
        String eventId = receiverEmail + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverEmail);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDto.createResponse(notification));
                }
        );
    }

    private Notify createNotification(User receiver, NotificationType notificationType, String content, MediaConvertMessage mediaConvertMessage) {
        return Notify.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .time(mediaConvertMessage.getTime())
                .videoUrl(mediaConvertMessage.getOutputPath())
                .thumbnailUrl(mediaConvertMessage.getThumbnailPath())
                .isRead(false)
                .build();
    }
}