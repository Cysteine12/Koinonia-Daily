package org.eni.koinoniadaily.modules.file;

import java.net.URL;
import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.file.dto.PresignedUploadUrlRequest;
import org.eni.koinoniadaily.modules.file.dto.PresignedUploadUrlResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class FileService {
  
  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final AppProperties props;

  public PresignedUploadUrlResponse generatePresignedUploadUrl(PresignedUploadUrlRequest request) {

    String objectKey = "thumbnails/" + UUID.randomUUID() + "." + request.getFileExtension();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                          .bucket(props.getAws().getS3BucketName())
                                          .key(objectKey)
                                          .contentType(getContentType(request.getFileExtension()))
                                          .acl(ObjectCannedACL.PUBLIC_READ)
                                          .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                                              .signatureDuration(Duration.ofMinutes(10)) 
                                              .putObjectRequest(putObjectRequest)
                                              .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
    
    URL presignedUrl = presignedRequest.url();

    URL objectUrl = s3Client.utilities().getUrl(builder -> 
                      builder.bucket(props.getAws().getS3BucketName())
                        .key(objectKey));

    return PresignedUploadUrlResponse.builder()
            .presignedUrl(presignedUrl.toString())
            .publicUrl(objectUrl.toString())
            .key(objectKey)
            .requiredHeaders(presignedRequest.signedHeaders())
            .build();
  }

  public void deleteObject(String objectKey) {

    if (!objectKey.startsWith("thumbnails/") || objectKey.contains("../")) {
      throw new ValidationException("Invalid object key");
    }

    DeleteObjectRequest request = DeleteObjectRequest.builder()
                                    .bucket(props.getAws().getS3BucketName())
                                    .key(objectKey)
                                    .build();

    s3Client.deleteObject(request);
  }

  private String getContentType(String fileExtension) {
    
    return switch (fileExtension.toLowerCase(Locale.ROOT)) {
      case "jpg", "jpeg" -> "image/jpeg";
      case "png" -> "image/png";
      case "gif" -> "image/gif";
      case "webp" -> "image/webp";
      default -> "application/octet-stream";
    };
  }
}
