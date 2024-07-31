package com.fizz.fizz_server.domain.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fizz.fizz_server.domain.file.dto.request.FinishUploadRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUploadInitiateRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUrlCreateRequest;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.config.properties.AwsProperties;
import com.fizz.fizz_server.global.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.*;

@Service
@RequiredArgsConstructor
public class FileService {
    public static final String VOD = "vod";
    public static final String UPLOAD_ID = "uploadId";
    public static final String PART_NUMBER = "partNumber";
    public static final String POST_IMAGE_PATH_FORMAT = "user/%s/post/%d/image/%s/%s";
    public static final String PROFILE_IMAGE_PATH_FORMAT = "user/%s/profile-image/%s/%s";

    private final AwsProperties awsProperties;
    private final AmazonS3 amazonS3Client;

    public String uploadFile(Long postId, Long userId, MultipartFile multipartFile) {
        this.validateFileExists(multipartFile);
        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String uuid = UUID.randomUUID().toString();
        String s3Path = String.format(POST_IMAGE_PATH_FORMAT, userId, postId, uuid, fileName);
        return uploadToS3(s3Path, multipartFile);
    }

    public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
        this.validateFileExists(multipartFile);
        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String uuid = UUID.randomUUID().toString();
        String s3Path = String.format(PROFILE_IMAGE_PATH_FORMAT, userId, uuid, fileName);
        return uploadToS3(s3Path, multipartFile);
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessException(FILE_NOT_FOUND);
        }
        if (multipartFile.getOriginalFilename() == null) {
            throw new BusinessException(FILENAME_NOT_FOUND);
        }
    }

    private String uploadToS3(String s3Path, MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(
                    awsProperties.getS3().getOutputBucket(), s3Path, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new BusinessException(FAIL_FILE_UPLOAD);
        }

        String cloudFrontDomain = awsProperties.getCloudFront().getDomain();
        return String.format("https://%s/%s", cloudFrontDomain, s3Path);
    }

    public InitiateMultipartUploadRequest initiateUpload(PreSignedUploadInitiateRequest request, Long postId, Long userId) {
        String fullName = request.originalFileName() + "." + request.fileFormat();
        String uuid = UUID.randomUUID().toString();
        String objectName = constructObjectName(userId, postId, uuid, fullName);
        ObjectMetadata objectMetadata = createObjectMetadata(request, fullName);

        return new InitiateMultipartUploadRequest(
                awsProperties.getS3().getInputBucket(),
                objectName,
                objectMetadata);
    }

    private String constructObjectName(Long userId, Long postId, String uuid, String fullName) {
        String folderStructure = String.format("user/%d/post/%d/%s/%s", userId, postId, uuid, fullName);
        return String.format("%s/%s", VOD, folderStructure);
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
                new GeneratePresignedUrlRequest(awsProperties.getS3().getInputBucket(), request.key())
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

        return new CompleteMultipartUploadRequest(
                awsProperties.getS3().getInputBucket(),
                finishUploadRequest.key(),
                finishUploadRequest.uploadId(),
                partETags
        );
    }


}
