package com.fizz.fizz_server.domain.file.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fizz.fizz_server.domain.file.dto.request.FinishUploadRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUploadInitiateRequest;
import com.fizz.fizz_server.domain.file.dto.request.PreSignedUrlCreateRequest;
import com.fizz.fizz_server.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final AmazonS3 amazonS3Client;

    /**
     * TODO. userId를 입력받아야 함. (token)
     */

    @PostMapping("/initiate-upload/post/{postId}")
    public InitiateMultipartUploadResult initiateUpload(@PathVariable Long postId,
                                                        @RequestBody PreSignedUploadInitiateRequest request) {
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = fileService.initiateUpload(request, postId);

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


}
