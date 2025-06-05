package com.vksfeng.quan.controller;

import com.vksfeng.quan.peerhub_pojo.dto.FeedCommentDTO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedCommentVO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// TODO 权限管理
@Tag(name = "动态模块")
@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @Operation(summary = "获取动态列表")
    @GetMapping
    public Result<PageResult> getFeedList(@RequestParam Integer page, @RequestParam Integer pageSize) {
        PageResult pageResult = feedService.getFeedList(page, pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "点赞动态")
    @PostMapping("/like")
    public Result like(@RequestBody Map<String, Long> request) {
        feedService.like(request.get("feed_id"));
        return Result.success();
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping("/{feedId}/like")
    public Result unlike(@PathVariable Long feedId) {
        feedService.unlike(feedId);
        return Result.success();
    }

    @Operation(summary = "评论动态")
    @PostMapping("/comment")
    public Result comment(@RequestBody FeedCommentDTO feedCommentDTO) {
        feedService.comment(feedCommentDTO);
        return Result.success();
    }

    @Operation(summary = "获取动态评论列表")
    @GetMapping("/comments")
    public Result<List<FeedCommentVO>> getCommentList(@RequestParam Long feedId) {
        List<FeedCommentVO> commentList = feedService.getCommentList(feedId);
        return Result.success(commentList);
    }

    @Operation(summary = "动态信息推送")
    @GetMapping("/recent")
    public Result<List<FeedVO>> getRecentFeed() {
        return feedService.getRecentFeed();
    }


}
