package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.achievement.util.AchievementEventPublisher;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.FavoriteAlreadyExistException;
import com.vksfeng.quan.exception.FriendshipAlreadyExistException;
import com.vksfeng.quan.exception.LikeAlreadyExistException;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.mapper.ResourceMapper;
import com.vksfeng.quan.resourcehub_pojo.dto.ResourceDTO;
import com.vksfeng.quan.resourcehub_pojo.entity.Resource;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceComment;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceFavorite;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceLike;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceCommentVO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FriendshipService;
import com.vksfeng.quan.service.ResourceHubService;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vksfeng.quan.constant.AchievementConstant.RESOURCE_SHARED;

@Service
public class ResourceHubServiceImpl implements ResourceHubService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private AchievementEventPublisher achievementEventPublisher;

    private Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    @Transactional
    public ResourceVO createResource(ResourceDTO resourceDTO) {
        Long userId = getUserId();
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDTO, resource);
        resource.setUserId(userId);
        resource.setCreatedAt(LocalDateTime.now());
        resource.setUpdatedAt(LocalDateTime.now());
        resourceMapper.createResource(resource);
        achievementEventPublisher.publish(userId, RESOURCE_SHARED, null);
        return resourceMapper.getResourceById(resource.getId(), userId);
    }

    public ResourceVO getResource(Long id) {
        ResourceVO resourceVO = resourceMapper.getResourceById(id, getUserId());
        if (resourceVO == null) {
            return null;
        }
        return resourceVO;
    }

//    public List<ResourceVO> getResourceList() {
//        Long userId = getUserId();
//        List<UserVO> friendId = friendshipService.getFriends(userId);
//        List<Long> userIds = new ArrayList<>(friendId.stream().map(UserVO::getId).toList());
//        userIds.add(userId);
//        List<ResourceVO> resourceList = resourceMapper.getResourceList(userIds, userId);
//        return resourceList;
//    }

    /**
     * 删除资源
     * 只有当资源拥有者才能删除，否则即时返回删除成功也没删除
     * @param id
     */
    public void deleteResource(Long id) {
        Long userId = getUserId();
        resourceMapper.deleteResource(id, userId);
    }

    public void addLike(Long id) {
        if (resourceMapper.isLiked(id, getUserId())) {
            throw new LikeAlreadyExistException();
        }
        ResourceLike resourceLike = new ResourceLike();
        resourceLike.setUserId(getUserId());
        resourceLike.setResourceId(id);
        resourceLike.setCreatedAt(LocalDateTime.now());
        resourceMapper.addLike(resourceLike);
    }

    public void removeLike(Long resourceId) {
        resourceMapper.removeLike(resourceId, getUserId());
    }

    @Transactional
    public ResourceCommentVO addComment(Long resourceId, String content) {
        ResourceComment resourceComment = ResourceComment.builder()
                .userId(getUserId())
                .resourceId(resourceId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        resourceMapper.addComment(resourceComment);
        if (resourceComment.getId() == null) {
            throw new RuntimeException("评论失败");
        }
        return resourceMapper.getResourceCommentById(resourceComment.getId());
    }

    public List<ResourceCommentVO> getCommentsByResourceId(Long id) {
        return resourceMapper.getCommentsByResourceId(id);
    }

    public void addFavorite(Long resourceId) {
        Long userId = getUserId();
        if (resourceMapper.isFavorite(resourceId, userId)) {
            throw new FavoriteAlreadyExistException("已经收藏过啦");
        }
        ResourceFavorite resourceFavorite = ResourceFavorite.builder()
                .userId(userId)
                .resourceId(resourceId)
                .createdAt(LocalDateTime.now())
                .build();
        resourceMapper.addFavorite(resourceFavorite);
    }

    public void removeFavorite(Long resourceId) {
        resourceMapper.removeFavorite(resourceId, getUserId());
    }

    public PageResult getResourceList(Integer page, Integer pageSize, String category) {
        Long userId = getUserId();
        List<UserVO> friendId = friendshipService.getFriends(userId);
        List<Long> userIds = new ArrayList<>(friendId.stream().map(UserVO::getId).toList());
        userIds.add(userId);
        Integer total = resourceMapper.getResourceCount(category, userIds);
        if (total <= 0) {
            return null;
        }
        Integer totalPage = (total + pageSize - 1) / pageSize;
        Integer offset = (page - 1) * pageSize;
        List<ResourceVO> resourceList = resourceMapper.getResourceList(userIds, userId, offset, pageSize, category);
        return new PageResult(totalPage, total, resourceList);
    }

    public PageResult getFavoriteList(Integer page, Integer pageSize, String category) {
        Long userId = getUserId();
        Integer total = resourceMapper.getFavoriteResourceCount(userId, category);
        if (total <= 0) {
            return null;
        }
        Integer totalPage = (total + pageSize - 1) / pageSize;
        Integer offset = (page - 1) * pageSize;
        List<ResourceVO> resourceList = resourceMapper.getFavoriteList(userId, offset, pageSize, category);
        return new PageResult(totalPage, total, resourceList);
    }

    @Override
    public Result<PageResult> getUserResourceList(Long userId, Integer page, Integer pageSize, String category) {
        if (!Objects.equals(userId, getUserId()) && friendshipService.isExist(getUserId(), userId)) {
            return Result.error("对方不是你的好友");
        }
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        Integer total = resourceMapper.getResourceCount(category, userIds);
        Integer totalPage = (total + pageSize - 1) / pageSize;
        Integer offset = (page - 1) * pageSize;
        if (total <= 0) {
            return Result.success(new PageResult(0, 0, null));
        }
        List<ResourceVO> resourceList = resourceMapper.getResourceList(userIds, userId, offset, pageSize, category);
        return Result.success(new PageResult(totalPage, total, resourceList));
    }

    @Override
    public Result<PageResult> getRecommendResourceList() {
        PageResult pageResult = getResourceList(1, 20, null);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return Result.success(new PageResult(0, 0, new ArrayList<>()));
        }

        // TODO 获取推荐资源，这里简单地随机抽取三个资源作为示例
        List<ResourceVO> resourceList = pageResult.getList();
        if (resourceList.size() <= 3) {
            return Result.success(new PageResult(1, resourceList.size(), resourceList));
        }

        // 随机抽取三个资源
        List<ResourceVO> recommendedResources = new ArrayList<>();
        java.util.Collections.shuffle(resourceList);
        recommendedResources.addAll(resourceList.subList(0, 3));

        return Result.success(new PageResult(1, 3, recommendedResources));
    }

    @Override
    public Integer getUserResourceCount(Long userId) {
        return resourceMapper.getResourceCount(null, List.of(userId));
    }

    @Override
    public Integer getUserReceivedLikes(Long userId) {
        return resourceMapper.getUserReceivedLikes(userId);
    }

    @Override
    public Integer getUserReceivedComments(Long userId) {
        return resourceMapper.getUserReceivedComments(userId);
    }

    @Override
    public Integer getUserReceivedFavorites(Long userId) {
        return resourceMapper.getUserReceivedFavorites(userId);
    }

}
