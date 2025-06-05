package com.vksfeng.quan.mapper;

import com.vksfeng.quan.resourcehub_pojo.entity.Resource;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceComment;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceFavorite;
import com.vksfeng.quan.resourcehub_pojo.entity.ResourceLike;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceCommentVO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    void createResource(Resource resource);

    ResourceVO getResourceById(Long id, Long currentUserId);

//    List<ResourceVO> getResourceList(List<Long> userIds, Long currentUserId);

    void deleteResource(Long id, Long userId);

    void addLike(ResourceLike resourceLike);

    boolean isLiked(Long userId, Long resourceId);

    void removeLike(Long resourceId, Long userId);

    void addComment(ResourceComment resourceComment);

    ResourceCommentVO getResourceCommentById(Long commentId);

    List<ResourceCommentVO> getCommentsByResourceId(Long id);

    void addFavorite(ResourceFavorite resourceFavorite);

    boolean isFavorite(Long resourceId, Long userId);

    void removeFavorite(Long resourceId, Long userId);

    Integer getResourceCount(String category, List<Long> userIds);

    List<ResourceVO> getResourceList(List<Long> userIds, Long currentUserId, Integer offset, Integer pageSize, String category);

    List<ResourceVO> getFavoriteList(Long userId, Integer offset, Integer pageSize, String category);

    Integer getFavoriteResourceCount(Long userId, String category);

    Integer getUserReceivedLikes(Long userId);

    Integer getUserReceivedComments(Long userId);

    Integer getUserReceivedFavorites(Long userId);
}
