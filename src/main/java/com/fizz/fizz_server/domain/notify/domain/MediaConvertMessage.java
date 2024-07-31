package com.fizz.fizz_server.domain.notify.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MediaConvertMessage {
    private String time;
    private String status;
    private String outputPath;
    private String thumbnailPath;
    private Long userId;

    public void updateUserMetadata(String cloudFrontDomain) {
        // CloudFront 도메인으로 URL 업데이트
        this.setOutputPath("https://" + cloudFrontDomain + outputPath + ".m3u8");
        this.setThumbnailPath("https://" + cloudFrontDomain + thumbnailPath + "thumbnails.0000000.jpg");
    }
}