package com.fizz.fizz_server.domain.notify.eventListener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferListener {

    @SqsListener(value = "${aws.sqs.queue-name}")
    public void messageListener(String message) {
        log.info("sns message : {}" , message);
    }
}
