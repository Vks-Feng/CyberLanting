package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.achievement.util.AchievementEventPublisher;
import com.vksfeng.quan.constant.RedisConstant;
import com.vksfeng.quan.context.BaseContext;
import com.vksfeng.quan.exception.FriendshipAlreadyExistException;
import com.vksfeng.quan.exception.NotLoginException;
import com.vksfeng.quan.mapper.FriendshipMapper;
import com.vksfeng.quan.peerhub_pojo.dto.FriendshipDTO;
import com.vksfeng.quan.peerhub_pojo.entity.Friendship;
import com.vksfeng.quan.peerhub_pojo.vo.UserSearchVO;
import com.vksfeng.quan.result.Result;
import com.vksfeng.quan.service.FriendshipService;
import com.vksfeng.quan.service.UserService;
import com.vksfeng.quan.user_pojo.entity.User;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.vksfeng.quan.constant.AchievementConstant.FRIEND_ADDED;

@Service
@Slf4j
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipMapper friendshipMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AchievementEventPublisher achievementEventPublisher;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    /**
     * 获取好友列表，双向，自己可是user可是friend
     * @param userId
     * @return
     */
    public List<UserVO> getFriends(Long userId) {
        return friendshipMapper.list(userId);
    }

    public boolean isExist(Long aId, Long bId) {
        List<Friendship> list = friendshipMapper.selectByTwoId(aId, bId);
        return !list.isEmpty();
    }

    @Override
    public Result<UserSearchVO> searchUser(Long id) {
        Long userId = getUserId();
        User user = userService.getUserById(id);
        if (user == null) {
            return Result.success("用户不存在");
        }
        UserSearchVO userSearchVO = new UserSearchVO();
        BeanUtils.copyProperties(user, userSearchVO);
        if (userId.equals(id) || isExist(userId, id)) {
            userSearchVO.setIsFriend(true);
        } else {
            userSearchVO.setIsFriend(false);
        }
        return Result.success(userSearchVO);
    }

    @Override
    public Result<List<UserSearchVO>> getNearbyUsers() {
        Long userId = getUserId();
        Boolean exists = stringRedisTemplate.opsForGeo()
                .position(RedisConstant.USER_LOCATION_KEY, userId.toString())
                .get(0) != null;

        if (!exists) {
            return Result.error("请先上传位置信息");
        }

        double radiusMeters = 5000;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().search(
                RedisConstant.USER_LOCATION_KEY,
                new GeoReference.GeoMemberReference(userId.toString()),  // 以当前用户为中心
                new Distance(radiusMeters, Metrics.METERS), // 指定查询半径
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                        .includeDistance()  // 返回距离信息
                        .sortAscending()    // 结果按距离从近到远排序
        );

        // 解析结果，提取 userId 列表
        List<Long> userIds = new ArrayList<>();
        if (results != null && !results.getContent().isEmpty()) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results.getContent()) {
                Long nearbyUserId = Long.valueOf(result.getContent().getName()); // 提取 userId
                if (!nearbyUserId.equals(userId)) {  // 过滤掉自己
                    userIds.add(nearbyUserId);
                }
            }
        } else {
            return Result.success(List.of());
        }
        if (userIds.isEmpty()) {
            return Result.success(List.of());
        }
        List<UserSearchVO> userSearchVOs = userService.searchUsers(userIds, userId);
        return Result.success(userSearchVOs);
    }

    /**
     * 添加好友请求：单向，自己是user
     * @param friendshipDTO
     */
    public void addFriendRequest(FriendshipDTO friendshipDTO) {
        if (isExist(friendshipDTO.getUserId(), friendshipDTO.getFriendId())) {
            throw new FriendshipAlreadyExistException("好友关系或好友申请已存在");
        }
        Friendship friendship = new Friendship();
        BeanUtils.copyProperties(friendshipDTO, friendship);
        friendship.setCreatedAt(LocalDateTime.now());
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipMapper.insert(friendship);
    }

    /**
     * 获取好友请求，单向，自己是friend
     * @param userId
     * @return
     */
    public List<UserVO> getRequests(Long userId) {
        return friendshipMapper.getRequests(userId);
    }

    /**
     * 接受好友请求，单向，自己是friend
     * @param friendshipDTO
     */
    public void acceptFriendRequest(FriendshipDTO friendshipDTO) {
        Friendship friendship = new Friendship();
        BeanUtils.copyProperties(friendshipDTO, friendship);
        friendship.setStatus("accepted");
        friendship.setUpdatedAt(LocalDateTime.now());
        friendshipMapper.update(friendship);
        achievementEventPublisher.publish(friendshipDTO.getUserId(), FRIEND_ADDED, null);
        achievementEventPublisher.publish(friendshipDTO.getFriendId(), FRIEND_ADDED, null);
    }

    /**
     * 删除好友，双向，自己可是user可是friend
     * @param friendshipDTO
     */
    public void removeFriend(FriendshipDTO friendshipDTO) {
        friendshipMapper.remove(friendshipDTO.getUserId(), friendshipDTO.getFriendId());
    }
}
