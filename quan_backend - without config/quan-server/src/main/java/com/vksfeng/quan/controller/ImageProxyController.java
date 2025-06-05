package com.vksfeng.quan.controller;

import com.vksfeng.quan.resourcehub_pojo.vo.ImageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ImageProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageProxyController {

    @Autowired
    private ImageProxyService imageProxyService;

    /**
     * 图片代理接口
     * @param url 外部图片 URL
     * @return 返回 Result 封装的图片数据或错误信息
     */
    @GetMapping("/img-proxy")
    public Result<ImageResult> getImage(@RequestParam String url) {
        if (url == null || url.isEmpty()) {
            return Result.error("缺少 URL 参数");
        }

        return imageProxyService.getImage(url);
    }
}
