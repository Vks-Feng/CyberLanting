package com.vksfeng.quan.service;

import com.vksfeng.quan.resourcehub_pojo.dto.ResourceDTO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceCommentVO;
import com.vksfeng.quan.resourcehub_pojo.vo.ResourceVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface ResourceHubService {
    ResourceVO createResource(ResourceDTO resourceDTO);

    ResourceVO getResource(Long id);

//    List<ResourceVO> getResourceList();

    void deleteResource(Long id);

    void addLike(Long id);

    void removeLike(Long id);

    ResourceCommentVO addComment(Long resourceId, String content);

    List<ResourceCommentVO> getCommentsByResourceId(Long id);

    void addFavorite(Long resourceId);

    void removeFavorite(Long resourceId);

    PageResult getResourceList(Integer page, Integer pageSize, String category);

    PageResult getFavoriteList(Integer page, Integer pageSize, String category);

    Result<PageResult> getUserResourceList(Long userId, Integer page, Integer pageSize, String category);

    Result<PageResult> getRecommendResourceList();

    Integer getUserResourceCount(Long userId);

    Integer getUserReceivedLikes(Long userId);

    Integer getUserReceivedComments(Long userId);

    Integer getUserReceivedFavorites(Long userId);
}
