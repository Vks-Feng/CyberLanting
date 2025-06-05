package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.FeedNotExistException;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.mapper.FeedMapper;
import com.vksfeng.quan.objectivehub_pojo.entity.Objective;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.peerhub_pojo.dto.FeedCommentDTO;
import com.vksfeng.quan.peerhub_pojo.entity.Feed;
import com.vksfeng.quan.peerhub_pojo.entity.FeedComment;
import com.vksfeng.quan.peerhub_pojo.entity.FeedLike;
import com.vksfeng.quan.peerhub_pojo.vo.FeedCommentVO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FeedService;
import com.vksfeng.quan.service.FriendshipService;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedMapper feedMapper;

    @Autowired
    private FriendshipService friendshipService;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    public PageResult getFeedList(Integer page, Integer pageSize) {
        // 1. 获取总记录数和总页数
        Integer total = feedMapper.count();
        Integer totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        // 2. 获取分页查询结果列表
        Integer offset = (page - 1) * pageSize;
        Long userId = getUserId();
        List<UserVO> friendId = friendshipService.getFriends(userId);
        List<Long> userIds = new ArrayList<>(friendId.stream().map(UserVO::getId).toList());
        userIds.add(userId);
        List<FeedVO> list = feedMapper.page(offset, pageSize, userIds, userId);

        return new PageResult(totalPage, total, list);
    }

    public void like(Long feedId) {
        // TODO 权限管理、仅能点赞自己与好友的动态
        Long userId = getUserId();
        if (feedMapper.getFeedById(feedId, userId) == null) {
            throw new FeedNotExistException("动态不存在");
        }
        FeedLike feedLike = FeedLike.builder()
                .feedId(feedId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        feedMapper.addFeedLike(feedLike);
    }


    public void unlike(Long feedId) {
        Long userId = getUserId();
        feedMapper.removeFeedLike(feedId, userId);
    }

    public void comment(FeedCommentDTO feedCommentDTO) {
        Long userId = getUserId();
        if (feedMapper.getFeedById(feedCommentDTO.getFeedId(), userId) == null) {
            throw new FeedNotExistException("动态不存在");
        }
        FeedComment feedComment = new FeedComment();
        feedComment.setFeedId(feedCommentDTO.getFeedId());
        feedComment.setContent(feedCommentDTO.getContent());
        feedComment.setUserId(userId);
        feedComment.setCreatedAt(LocalDateTime.now());
        feedMapper.addFeedComment(feedComment);
    }

    public List<FeedCommentVO> getCommentList(Long feedId) {
        if (feedMapper.getFeedById(feedId, getUserId()) == null) {
            throw new FeedNotExistException("动态不存在");
        }
        return feedMapper.getCommentList(feedId);
    }

    public void autoPostTaskCompletion(Task task) {
        Long userId = getUserId();
        Feed feed = Feed.builder()
                .userId(userId)
                .type("task_completed")
                .relatedId(task.getId())
                .content("完成了任务：" + task.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        feedMapper.insertFeed(feed);
    }

    public void autoPostObjectiveCompletion(Objective objective) {
        Long userId = getUserId();
        Feed feed = Feed.builder()
                .userId(userId)
                .type("objective_completed")
                .relatedId(objective.getId())
                .content("完成了目标：" + objective.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        feedMapper.insertFeed(feed);
    }

    @Override
    public Result<List<FeedVO>> getRecentFeed() {
        PageResult result = getFeedList(1, 3);
        List<FeedVO> list = result.getList();
        if (list != null && !list.isEmpty()) {
            return Result.success(list);
        } else {
            return Result.success(new ArrayList<>());
        }
    }

}
