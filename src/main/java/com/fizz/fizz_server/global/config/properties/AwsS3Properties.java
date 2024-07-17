package com.fizz.fizz_server.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
public class AwsS3Properties {
	private String accessKey;
	private String secretKey;
	private S3 s3;

	@Getter
	@Setter
	public static class S3 {
		private String bucket;
	}
}
