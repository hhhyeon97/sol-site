package com.sol.shop.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class S3Service2 {

    private final S3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service2(S3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadImageToS3(MultipartFile imageFile, String dirName) {
        try {
            File uploadFile = convertMultipartFileToFile(imageFile);
            String fileName = dirName + "/" + uploadFile.getName();
            String uploadImageUrl = uploadToS3(uploadFile, fileName);
            deleteLocalFile(uploadFile);
            return uploadImageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
    }

    private String uploadToS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build(),
                uploadFile.toPath()
        );
        return amazonS3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(fileName).build()).toExternalForm();
    }

    private void deleteLocalFile(File file) {
        if (file.delete()) {
            System.out.println("로컬 파일 삭제 성공");
        } else {
            System.out.println("로컬 파일 삭제 실패");
        }
    }
}
