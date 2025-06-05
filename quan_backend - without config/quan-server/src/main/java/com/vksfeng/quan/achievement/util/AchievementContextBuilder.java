package com.vksfeng.quan.achievement.util;

import com.vksfeng.quan.achievement.pojo.entity.AchievementTriggerEvent;
import com.vksfeng.quan.mapper.FriendshipMapper;
import com.vksfeng.quan.mapper.ObjectiveMapper;
import com.vksfeng.quan.mapper.TaskMapper;
import com.vksfeng.quan.service.ResourceHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.vksfeng.quan.constant.AchievementConstant.*;

@Component
public class AchievementContextBuilder {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private ObjectiveMapper objectiveMapper;
    @Autowired
    private ResourceHubService resourceHubService;

    public Map<String, Object> buildContext(AchievementTriggerEvent event) {
        Long userId = event.getUserId();
        String type = event.getType();

        Map<String, Object> ctx = new HashMap<>();

        switch (type) {
            case OBJECTIVE_ADDED -> {
                ctx.put("createdObjectives", objectiveMapper.getObjectiveCount(userId));
            }
            case TASK_COMPLETED -> {
                ctx.put("completedTasks", taskMapper.getCompletedTaskCount(userId));
            }
            case FRIEND_ADDED -> {
                ctx.put("friendsCount", friendshipMapper.countFriends(userId));
            }
            case RESOURCE_SHARED -> {
                ctx.put("sharedResources", resourceHubService.getUserResourceCount(userId));
                ctx.put("resourceLikesReceived", resourceHubService.getUserReceivedLikes(userId));
                ctx.put("resourceCommentsReceived", resourceHubService.getUserReceivedComments(userId));
            }

            // 扩展类型
        }

        return ctx;
    }
}
