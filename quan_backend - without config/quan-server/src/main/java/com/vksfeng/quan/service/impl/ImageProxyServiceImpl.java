package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.resourcehub_pojo.vo.ImageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ImageProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class ImageProxyServiceImpl implements ImageProxyService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 代理图片请求
     * @param imageUrl 外部图片的 URL
     * @return 返回 Result 封装的图片数据
     */
    public Result<ImageResult> getImage(String imageUrl) {
        try {
            // 获取图片内容
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrl, HttpMethod.GET, null, byte[].class);
            byte[] imageBytes = response.getBody();
            String contentType = response.getHeaders().getContentType().toString(); // 例如 image/png

            // 编码为 base64 字符串
            String base64 = Base64.getEncoder().encodeToString(imageBytes);

            // 返回包含 base64 和 contentType 的 ImageResult
            ImageResult imageResult = new ImageResult(base64, contentType);
            return Result.success(imageResult);
        } catch (Exception e) {
            return Result.error("图片代理失败: " + e.getMessage());
        }
    }
}
