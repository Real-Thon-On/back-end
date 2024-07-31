package com.fizz.fizz_server.global.config;

import com.fizz.fizz_server.global.config.properties.AwsS3Properties;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@EnableConfigurationProperties(AwsS3Properties.class)
@RequiredArgsConstructor
@Configuration
public class AwsSqsListenerConfig {

    private final AwsS3Properties awsS3Properties;

    @Primary
    // 클라이언트 설정
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return awsS3Properties.getAccessKey();
                    }
                    @Override
                    public String secretAccessKey() {
                        return awsS3Properties.getSecretKey();
                    }
                })
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }
}
