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
    private final AwsS3Properties awsS3Properties;

    public InitiateMultipartUploadRequest initiateUpload(PreSignedUploadInitiateRequest request, Long userId) {
        String rootFolder = getFileTypeRootFolder(request.originalFileName());
        String objectName = getObjectName(rootFolder, request.fileType(), userId, request.originalFileName());
        ObjectMetadata objectMetadata = getObjectMetadata(request);

        return new InitiateMultipartUploadRequest(
                awsS3Properties.getS3().getBucket(),
                objectName,
                objectMetadata);
    }

    private String getFileTypeRootFolder(String fileType) {
        String fileExtension = URLConnection.guessContentTypeFromName(fileType);
        if (fileExtension != null && fileExtension.startsWith(IMAGE)) {
            return IMAGE;
        } else {
            return VOD;
        }
    }

    private String getObjectName(String rootFolder, String fileType, Long userId, String originalFileName) {
        return String.format("%s/%s/%s/%d/%s/%s",
                rootFolder,
                "dev", // TODO. 하드코딩 제거
                fileType,
                userId,
                UUID.randomUUID(),
                originalFileName);
    }

    private ObjectMetadata getObjectMetadata(PreSignedUploadInitiateRequest request) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(request.fileSize());
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(request.originalFileName()));
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

        generatePresignedUrlRequest.addRequestParameter("uploadId", request.uploadId());
        generatePresignedUrlRequest.addRequestParameter("partNumber", String.valueOf(request.partNumber()));
        return generatePresignedUrlRequest;
    }

    public CompleteMultipartUploadRequest completeUpload(FinishUploadRequest finishUploadRequest) {
        List<PartETag> partETags = finishUploadRequest.parts().stream()
                .map(part -> new PartETag(part.partNumber(), part.eTag()))
                .toList();

        // TODO. DB 저장 및 업로드 url 설정 (copy 작업 및 job 완료 시에 cloud watch event 는 오직 https 라 고민)

        return new CompleteMultipartUploadRequest(
                awsS3Properties.getS3().getBucket(),
                finishUploadRequest.key(),
                finishUploadRequest.uploadId(),
                partETags
        );
    }
}
