package com.vksfeng.quan.controller;

import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public Result uploadAvatar(MultipartFile file) throws FileUploadException {
        if (file == null || file.isEmpty()) {
            return Result.error("文件为空");
        }
        return fileService.uploadFile(file);
    }
}
