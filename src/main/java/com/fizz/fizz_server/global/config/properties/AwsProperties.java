package com.fizz.fizz_server.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
	private String accessKey;
	private String secretKey;
	private S3 s3;
	private CloudFront cloudFront;

	@Getter
	@Setter
	public static class S3 {
		private String inputBucket;
		private String outputBucket;
	}

	@Getter
	@Setter
	public static class CloudFront {
		private String domain;
	}
}
