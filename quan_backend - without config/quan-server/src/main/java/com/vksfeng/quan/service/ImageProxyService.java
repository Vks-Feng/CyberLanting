package com.vksfeng.quan.service;

import com.vksfeng.quan.resourcehub_pojo.vo.ImageResult;
import com.vksfeng.quan.result.Result;

public interface ImageProxyService {
    Result<ImageResult> getImage(String url);
}
