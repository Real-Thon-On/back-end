package com.fizz.fizz_server.global.config;

import com.fizz.fizz_server.global.config.properties.AwsProperties;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@EnableConfigurationProperties(AwsProperties.class)
@RequiredArgsConstructor
@Configuration
public class AwsSqsListenerConfig {

    private final AwsProperties awsProperties;

    @Primary
    // 클라이언트 설정
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return awsProperties.getAccessKey();
                    }
                    @Override
                    public String secretAccessKey() {
                        return awsProperties.getSecretKey();
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
