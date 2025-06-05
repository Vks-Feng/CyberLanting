package com.vksfeng.quan.controller;

import cn.hutool.db.Page;
import com.vksfeng.quan.resourcehub_pojo.dto.ResourceDTO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceCommentVO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.ResourceHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "资源中心模块")
@RestController
@RequestMapping("/resources")
@Slf4j
public class ResourceHubController {

    @Autowired
    private ResourceHubService resourceHubService;

    @Operation(summary = "创建资源")
    @PostMapping
    public Result<ResourceVO> createResource(@RequestBody ResourceDTO resourceDTO) {
        ResourceVO resourceVO = resourceHubService.createResource(resourceDTO);
        return Result.success(resourceVO);
    }

    @Operation(summary = "获取资源")
    @GetMapping("/{id}")
    public Result<ResourceVO> getResource(@PathVariable Long id) {
        ResourceVO resourceVO = resourceHubService.getResource(id);
        if (resourceVO == null) {
            return Result.error("资源不存在");
        }
        return Result.success(resourceVO);
    }

    @Operation(summary = "获取资源列表")
    @GetMapping
    public Result<PageResult> getResourceList(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String category) {
        PageResult pageResult = resourceHubService.getResourceList(page, pageSize, category);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取推荐资源列表")
    @GetMapping("/recommend")
    public Result<PageResult> getRecommendResourceList() {
        return resourceHubService.getRecommendResourceList();
    }

    @Operation(summary = "获取指定id用户的资源")
    @GetMapping("/user/{userId}")
    public Result<PageResult> getUserResourceList(@PathVariable Long userId, @RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String category) {
        return resourceHubService.getUserResourceList(userId, page, pageSize, category);
    }


    @Operation(summary = "删除资源")
    @DeleteMapping("/{id}")
    public Result deleteResource(@PathVariable Long id) {
        // TODO 删除关联点赞与评论等
        resourceHubService.deleteResource(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "点赞资源")
    @PostMapping("/{id}/like")
    public Result likeResource(@PathVariable Long id) {
        resourceHubService.addLike(id);
        return Result.success("点赞成功");
    }

    @Operation(summary = "取消点赞资源")
    @DeleteMapping("/{id}/like")
    public Result unlikeResource(@PathVariable Long id) {
        resourceHubService.removeLike(id);
        return Result.success("取消点赞成功");
    }

    @Operation(summary = "发布评论")
    @PostMapping("/{id}/comments")
    public Result addComment(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        ResourceCommentVO resourceCommentVO = resourceHubService.addComment(id, content);
        if (resourceCommentVO == null) {
            return Result.error("评论失败");
        }
        return Result.success(resourceCommentVO);
    }

    @Operation(summary = "获取指定帖子的评论")
    @GetMapping("/{id}/comments")
    public Result<List<ResourceCommentVO>> getComments(@PathVariable Long id) {
        List<ResourceCommentVO> resourceCommentVOList = resourceHubService.getCommentsByResourceId(id);
        return Result.success(resourceCommentVOList);
    }

    @Operation(summary = "收藏资源")
    @PostMapping("/{id}/favorite")
    public Result favoriteResource(@PathVariable Long id) {
        resourceHubService.addFavorite(id);
        return Result.success("收藏成功");
    }

    @Operation(summary = "取消收藏资源")
    @DeleteMapping("/{id}/favorite")
    public Result unfavoriteResource(@PathVariable Long id) {
        resourceHubService.removeFavorite(id);
        return Result.success("取消收藏成功");
    }

    @Operation(summary = "获取收藏列表")
    @GetMapping("/favorite")
    public Result<PageResult> getFavoriteList(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String category) {
        PageResult resourceVOList = resourceHubService.getFavoriteList(page, pageSize, category);
        return Result.success(resourceVOList);
    }


}
