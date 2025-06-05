package com.vksfeng.quan.service;

import com.vksfeng.quan.objectivehub_pojo.entity.Objective;
import com.vksfeng.quan.objectivehub_pojo.entity.Task;
import com.vksfeng.quan.peerhub_pojo.dto.FeedCommentDTO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedCommentVO;
import com.vksfeng.quan.peerhub_pojo.vo.FeedVO;
import com.vksfeng.quan.result.PageResult;
import com.vksfeng.quan.result.Result;

import java.util.List;

public interface FeedService {
    PageResult getFeedList(Integer page, Integer pageSize);

    void like(Long feedId);

    void unlike(Long feedId);

    void comment(FeedCommentDTO feedCommentDTO);

    List<FeedCommentVO> getCommentList(Long feedId);

    void autoPostTaskCompletion(Task task);

    void autoPostObjectiveCompletion(Objective objective);

    Result<List<FeedVO>> getRecentFeed();
}
