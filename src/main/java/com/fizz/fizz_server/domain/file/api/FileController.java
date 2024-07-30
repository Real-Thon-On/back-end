package com.fizz.fizz_server.domain.file.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fizz.fizz_server.domain.file.dto.request.AwsLambdaCompleteRequest;
import com.fizz.fizz_server.domain.file.dto.request.FinishUploadRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUploadInitiateRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUrlCreateRequest;
import com.fizz.fizz_server.domain.file.service.FileService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final AmazonS3 amazonS3Client;

    /**
     * TODO. userId를 입력받아야 함. (token)
     */

    @PostMapping("/initiate-upload/post/{postId}")
    public InitiateMultipartUploadResult initiateUploadToPost(@PathVariable Long postId,
                                                              @RequestBody @Valid PreSignedUploadInitiateRequest request) {
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = fileService.initiateUpload(request, postId, null);

        return amazonS3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
    }

    @PostMapping("/initiate-upload")
    public InitiateMultipartUploadResult initiateUploadToUser(@RequestBody @Valid PreSignedUploadInitiateRequest request,
                                                              @AuthenticationPrincipal CustomUserPrincipal user) {
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = fileService.initiateUpload(request, null, user.getUserId());

        return amazonS3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
    }

    @PostMapping("/presigned-url")
    public URL preSignedUrl(@RequestBody PreSignedUrlCreateRequest request) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = fileService.preSignedUrl(request);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @PostMapping("/complete-upload")
    public CompleteMultipartUploadResult completeUpload(@RequestBody FinishUploadRequest finishUploadRequest) {
        CompleteMultipartUploadRequest completeMultipartUploadRequest = fileService.completeUpload(finishUploadRequest);

        return amazonS3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    @PostMapping("/complete-upload/aws-lambda")
    public String completeUploadAws(@RequestBody AwsLambdaCompleteRequest request) {
        log.info("type : {} , urls : {}", request.type(), request.urls());
        log.info("성공");
        return "성공";
    }
}
