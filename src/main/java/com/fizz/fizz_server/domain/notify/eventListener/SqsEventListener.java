package com.fizz.fizz_server.domain.notify.eventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fizz.fizz_server.domain.notify.domain.NotificationType;
import com.fizz.fizz_server.domain.notify.domain.MediaConvertMessage;
import com.fizz.fizz_server.domain.notify.service.NotifyService;
import com.fizz.fizz_server.global.config.properties.AwsProperties;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.fizz.fizz_server.domain.notify.domain.MediaConvertStatus.COMPLETE;
import static com.fizz.fizz_server.domain.notify.domain.MediaConvertStatus.ERROR;
import static com.fizz.fizz_server.domain.notify.domain.NotificationType.ENCODING_ERROR;
import static com.fizz.fizz_server.domain.notify.domain.NotificationType.ENCODING_FINISH;

@RequiredArgsConstructor
@Slf4j
@Component
public class SqsEventListener {

    private final ObjectMapper objectMapper;
    private final NotifyService notifyService;
    private final AwsProperties awsProperties;

    @SqsListener(value = "${aws.sqs.queue-name}")
    public void messageListener(String message) throws JsonProcessingException {
        MediaConvertMessage mediaConvertMessage = messageParser(message);
        String status = mediaConvertMessage.getStatus();

        switch (status) {
            case COMPLETE:
                sendNotification(mediaConvertMessage, ENCODING_FINISH, "영상 업로드가 완료되었습니다!");
                break;
            case ERROR:
                sendNotification(mediaConvertMessage, ENCODING_ERROR, "영상 업로드에 실패했습니다!");
                break;
            default:
                log.warn("Unhandled status: {}", status);
                break;
        }
    }

    private void sendNotification(MediaConvertMessage mediaConvertMessage, NotificationType notificationType, String message) {
        log.info("Media Convert final status: {}", mediaConvertMessage.getStatus());
        notifyService.send(mediaConvertMessage, notificationType, message);
    }

    private MediaConvertMessage messageParser(String snsMessage) throws JsonProcessingException {
        try {
            JsonNode rootNode = objectMapper.readTree(snsMessage);

            String message = rootNode.get("Message").asText();
            JsonNode messageNode = objectMapper.readTree(message);

            MediaConvertMessage mediaConvertMessage = new MediaConvertMessage();
            mediaConvertMessage.setTime(messageNode.get("time").asText());
            mediaConvertMessage.setStatus(messageNode.get("detail").get("status").asText());
            mediaConvertMessage.setOutputPath(messageNode.get("detail").get("userMetadata").get("outputPath").asText());
            mediaConvertMessage.setThumbnailPath(messageNode.get("detail").get("userMetadata").get("thumbnailPath").asText());
            mediaConvertMessage.setUserId(Long.valueOf(messageNode.get("detail").get("userMetadata").get("userId").asText()));

            mediaConvertMessage.updateUserMetadata(awsProperties.getCloudFront().getDomain());
            return mediaConvertMessage;
        } catch (Exception e) {
            log.error("Failed to parse SNS message: {}", e.getMessage());
            throw e;
        }
    }
}
