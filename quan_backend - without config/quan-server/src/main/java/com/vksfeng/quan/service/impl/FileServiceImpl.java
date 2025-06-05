package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FileService;
import com.vksfeng.quan.util.AliOSSUtils;
//import com.vksfeng.quan.util.MinioUtils;
//import io.minio.MinioAsyncClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

//    @Autowired
//    private MinioUtils minioUtils;

    @Autowired
    private AliOSSUtils aliOSSUtils;

//    public Result uploadFile(MultipartFile file) throws FileUploadException {
//        log.info("文件上传：{}", file);
//        try {
//            return Result.success(minioUtils.uploadFile(file));
//        } catch (IOException e) {
//            log.error("文件上传失败：{}", e);
//        }
//        return Result.error("文件上传失败");
//    }

    public Result uploadFile(MultipartFile file) throws FileUploadException {
        log.info("文件上传：{}", file);
        try {
            //原始文件名
            /*首先通过file.getOriginalFilename()获取原始文件名*/
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            /*然后通过originalFilename.lastIndexOf(".")获取文件名的后缀。*/
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            /*使用UUID.randomUUID().toString()生成一个随机的文件名，并将后缀拼接在文件名后面，构造出新的文件名。*/
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            /*然后，调用aliOssUtil.upload方法将文件上传到OSS，并获取文件的请求路径。*/
            String filePath = aliOSSUtils.upload(file.getBytes(), objectName);
            /*最后，返回一个Result对象，其中包含上传文件的请求路径。*/
            return Result.success((Object)filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error("文件上传失败");
    }

}
