package com.vksfeng.quan.mapper;

import com.vksfeng.quan.peerhub_pojo.dto.FeedCommentDTO;
import com.vksfeng.quan.peerhub_pojo.entity.Feed;
import com.vksfeng.quan.peerhub_pojo.entity.FeedComment;
import com.vksfeng.quan.peerhub_pojo.entity.FeedLike;
import com.vksfeng.quan.peerhub_pojo.vo.FeedCommentVO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    Integer count();

    List<FeedVO> page(Integer offset, Integer pageSize, List<Long> userIds, Long userId);

    void addFeedLike(FeedLike feedLike);

    FeedVO getFeedById(Long feedId, Long userId);

    void addFeedComment(FeedComment feedComment);

    void removeFeedLike(Long feedId, Long userId);

    List<FeedCommentVO> getCommentList(Long feedId);

    void insertFeed(Feed feed);

    FeedLike getFeedLike(Long feedId, Long userId);
}
