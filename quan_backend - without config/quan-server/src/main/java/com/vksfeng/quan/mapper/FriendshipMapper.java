package com.vksfeng.quan.mapper;

import com.vksfeng.quan.peerhub_pojo.dto.FriendshipDTO;
import com.vksfeng.quan.peerhub_pojo.entity.Friendship;
import com.vksfeng.quan.user_pojo.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendshipMapper {

    Integer countFriends(Long userId);

    List<UserVO> list(Long userId);

    void insert(Friendship friendship);

    List<UserVO> getRequests(Long userId);

    void update(Friendship friendship);

    void remove(Long userId, Long friendId);

    List<Friendship> selectByTwoId(Long aId, Long bId);
}
