package com.vksfeng.quan.service;

import com.vksfeng.quan.result.Result;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Result uploadFile(MultipartFile file) throws FileUploadException;
}
