package com.example.demo.Services;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class S3services {
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            File fileObj = convertMultiPartFileToFile(file);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            s3Client.putObject(bucketName, fileName, fileObj);
            fileObj.delete();
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String getFileUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
    public List<String> getAllImageUrls() {
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<String> urls = new ArrayList<>();
        for (S3ObjectSummary object : result.getObjectSummaries()) {
            if (object.getKey().toLowerCase().endsWith(".jpg") ||
                    object.getKey().toLowerCase().endsWith(".jpeg") ||
                    object.getKey().toLowerCase().endsWith(".png")) {
                urls.add(getFileUrl(object.getKey()));
            }
        }
        return urls;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}
