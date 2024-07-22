package com.fizz.fizz_server.domain.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.*;
import com.fizz.fizz_server.domain.file.dto.request.FinishUploadRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUploadInitiateRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUrlCreateRequest;
import com.fizz.fizz_server.global.config.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    public static final String IMAGE = "image";
    public static final String VOD = "vod";
    public static final String UPLOAD_ID = "uploadId";
    public static final String PART_NUMBER = "partNumber";

    private final AwsS3Properties awsS3Properties;

    public InitiateMultipartUploadRequest initiateUpload(PreSignedUploadInitiateRequest request, Long postId, Long userId) {
        String fullName = request.originalFileName() + "." + request.fileFormat();
        String fileType = this.determineFileType(fullName);
        String uuid = UUID.randomUUID().toString();
        String objectName = this.constructObjectName(fileType, request.fileFormat(), postId, userId, uuid, fullName);
        ObjectMetadata objectMetadata = this.createObjectMetadata(request, fullName);

        return new InitiateMultipartUploadRequest(
                awsS3Properties.getS3().getBucket(),
                objectName,
                objectMetadata);
    }


    private String determineFileType(String fullName) {
        String fileExtension = URLConnection.guessContentTypeFromName(fullName);
        return (fileExtension != null && fileExtension.startsWith(IMAGE)) ? IMAGE : VOD;
    }

    private String constructObjectName(String fileType, String fileFormat, Long postId, Long userId, String uuid, String fullName) {
        String folderType = (postId != null) ? "post" : "user";
        return String.format("%s/%s/%s/%d/%s/%s",
                folderType, fileType, fileFormat, (postId != null ? postId : userId), uuid, fullName);
    }

    private ObjectMetadata createObjectMetadata(PreSignedUploadInitiateRequest request, String fullName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(request.fileSize());
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(fullName));
        return objectMetadata;
    }

    public GeneratePresignedUrlRequest preSignedUrl(PreSignedUrlCreateRequest request) {
        Date expirationTime = Date.from(
                LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()
        );

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(awsS3Properties.getS3().getBucket(), request.key())
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expirationTime); // presigned url 만료 시간 설정

        generatePresignedUrlRequest.addRequestParameter(UPLOAD_ID, request.uploadId());
        generatePresignedUrlRequest.addRequestParameter(PART_NUMBER, String.valueOf(request.partNumber()));
        return generatePresignedUrlRequest;
    }

    public CompleteMultipartUploadRequest completeUpload(FinishUploadRequest finishUploadRequest) {
        List<PartETag> partETags = finishUploadRequest.parts().stream()
                .map(part -> new PartETag(part.partNumber(), part.eTag()))
                .toList();

        /**
         * TODO. DB 저장 및 업로드 url 설정 (copy 작업 및 job 완료 시에 cloud watch event 는 오직 https 라 고민)
         * 최종 url
         * image : {cloudFrontUrl}/{mediaType}/dev/{fileFormat}/{uuid}/{filename}.{fileFormat}
         * vod : {cloudFrontUrl}/{mediaType}/dev/hls/{uuid}/{filename}.m3u8
         */

        return new CompleteMultipartUploadRequest(
                awsS3Properties.getS3().getBucket(),
                finishUploadRequest.key(),
                finishUploadRequest.uploadId(),
                partETags
        );
    }
}
