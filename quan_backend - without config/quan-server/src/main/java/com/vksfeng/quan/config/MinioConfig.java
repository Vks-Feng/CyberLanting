//package com.vksfeng.quan.config;
//
//import com.vksfeng.quan.util.MinioUtils;
//import io.minio.MinioClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MinioConfig {
//
//    @Value("${minio.endpoint}")
//    private String minioEndpoint;
//
//    @Value("${minio.access-key}")
//    private String minioAccessKey;
//
//    @Value("${minio.secret-key}")
//    private String minioSecretKey;
//
//    @Value("${minio.bucket-name}")
//    private String bucketName;
//
//    @Bean
//    public MinioClient getMinioClient() {
//        return MinioClient.builder()
//                .endpoint(minioEndpoint)
//                .credentials(minioAccessKey, minioSecretKey)
//                .build();
//    }
//
//    @Bean
//    public MinioUtils minioUtils(MinioClient minioClient) {
//        return new MinioUtils(minioClient, bucketName);
//    }
//
//}
