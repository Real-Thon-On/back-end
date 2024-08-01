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
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final AmazonS3 amazonS3Client;

    /**
     * 게시글 이미지 업로드
     */
    @PostMapping("/image/upload")
    public String uploadFile(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                             @RequestPart(value = "file") MultipartFile multipartFile) {
        return fileService.uploadFile(userPrincipal.getUserId(), multipartFile);
    }

    /**
     * 사용자 프로필 이미지 업로드
     */
    @PostMapping("/profile-image/upload")
    public String uploadProfileImage(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                     @RequestPart(value = "file") MultipartFile multipartFile) {
        return fileService.uploadProfileImage(userPrincipal.getUserId(), multipartFile);
    }

    /**
     * 동영상 업로드를 위한 업로드 시작 요청
     */
    @PostMapping("/initiate-upload")
    public InitiateMultipartUploadResult initiateUploadToPost(@RequestBody @Valid PreSignedUploadInitiateRequest request,
                                                              @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = fileService.initiateUpload(request, userPrincipal.getUserId());

        return amazonS3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
    }

    /**
     * Pre-Signed URL 발급받기
     */
    @PostMapping("/presigned-url")
    public URL preSignedUrl(@RequestBody @Valid PreSignedUrlCreateRequest request) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = fileService.preSignedUrl(request);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * 업로드를 성공적으로 완료했다는 요청
     */
    @PostMapping("/complete-upload")
    public CompleteMultipartUploadResult completeUpload(@RequestBody @Valid FinishUploadRequest finishUploadRequest) {
        CompleteMultipartUploadRequest completeMultipartUploadRequest = fileService.completeUpload(finishUploadRequest);

        return amazonS3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }
}
