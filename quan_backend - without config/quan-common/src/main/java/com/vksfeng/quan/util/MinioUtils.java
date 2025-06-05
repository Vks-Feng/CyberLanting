//package com.vksfeng.quan.util;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.IdUtil;
//import io.minio.*;
//import io.minio.errors.*;
//import io.minio.http.Method;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.http.fileupload.FileUploadException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.util.concurrent.TimeUnit;
//
//
//
//@Slf4j
//@Component
//public class MinioUtils {
//
//    private final MinioClient minioClient;
//    private final String bucketName;
//
//    public MinioUtils(MinioClient minioClient, String bucketName) {
//        this.minioClient = minioClient;
//        this.bucketName = bucketName;
//    }
//
//    private final int presignedUrlExpireDay = 7;
//
//    public String uploadFile(MultipartFile file) throws FileUploadException {
//        try {
//            String objectId = generateFileName(file);
//            uploadToMinio(file, objectId);
//            String url = generatePresignedUrl(objectId);
//            if (url == null || url.isEmpty()) {
//                throw new FileUploadException("文件上传成功但无法生成 URL");
//            }
//            return url;
//        } catch (Exception e) {
//            log.error("MinIO 文件上传失败", e);
//            throw new FileUploadException("文件上传失败", e);
//        }
//    }
//
//    private String generateFileName(MultipartFile file) {
//        String originalFilename = file.getOriginalFilename();
//        String suffix = (originalFilename != null) ? FileUtil.getSuffix(originalFilename) : "";
//        suffix = suffix.isEmpty() ? "bin" : suffix;
//        String uuid = IdUtil.simpleUUID();
//        return DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd") + "/" + uuid + "." + suffix;
//    }
//
//    private void uploadToMinio(MultipartFile file, String objectId) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
//        try {
//            PutObjectArgs putArgs = PutObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(objectId)
//                    .contentType(file.getContentType())
//                    .stream(file.getInputStream(), file.getSize(), -1)
//                    .build();
//            minioClient.putObject(putArgs);
//        } catch (Exception e) {
////            throw new MinioException();
//            throw e;
//        }
//    }
//
//    private String generatePresignedUrl(String objectId) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
//        try {
//            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
//                    .bucket(bucketName)
//                    .object(objectId)
//                    .method(Method.GET)
//                    .expiry(presignedUrlExpireDay, TimeUnit.DAYS)
//                    .build();
//            return minioClient.getPresignedObjectUrl(args);
//        } catch (Exception e) {
//            throw e;
////            throw new MinioException();
//        }
//    }
//}
