package com.fizz.fizz_server.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fizz.fizz_server.global.config.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AwsS3Properties.class)
public class AwsS3Config {

	private final AwsS3Properties awsS3Properties;

	@Bean
	public AmazonS3 amazonS3Client() {
		return AmazonS3ClientBuilder.standard()
			.withCredentials(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsS3Properties.getAccessKey(),
					awsS3Properties.getSecretKey())))
			.withRegion(Regions.AP_NORTHEAST_2)
			.build();
	}
}
