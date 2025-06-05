import { LayoutList } from "lucide-vue-next";
import { axiosClient } from "./axios-api";
import { ElNotification } from "element-plus";

// 获取好友列表√
const getFriends = async () => {
  try {
    const response = await axiosClient.get("/friends/list");
    console.log("friendlist", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取好友列表失败",
      type: "error",
    });
    console.error("获取好友列表失败", error);
    return null;
  }
};

//生成好友数组
const loadFriends = async () => {
  try {
    const response = await getFriends();

    return Array.isArray(response) ? response : [];
  } catch (error) {
    console.error("Failed to fetch friends", error);
    return [];
  }
};

// 获取好友详细信息
const getFriendProfile = async (friendId) => {
  try {
    const response = await axiosClient.get("/friends/profile", {
      params: { friendId },
    });
    console.log("friendprofile", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取好友详细信息失败",
      type: "error",
    });
    console.error("Failed to get friend profile", error);
    return null;
  }
};

const searchUsers = async (id) => {
  try {
    const response = await axiosClient.get(`/friends/search?id=${id}`); ;
    console.log("搜索用户", response);
    return response;
  } catch (error) {
    console.error("Failed to search user", error);
    throw error;
  } 
};

// 发送好友请求√
const postAddFriend = async (friendId) => {
  try {
    const response = await axiosClient.post("/friends/request", {
      friendId,
    });
    console.log("post addfriendRequest Successfully", response);
    return response;
  } catch (error) {
    console.error("Failed to add friend", error);
    throw error;
  }
};

// 接受好友请求√
const postAcceptFriend = async (userId) => {
  try {
    const response = await axiosClient.post("/friends/accept", {
      userId,
    });
    console.log("post acceptfriendRequest Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "接受好友请求失败",
      type: "error",
    });
    console.error("Failed to accept friend", error);
    throw error;
  }
};

// 删除好友√
const postRemoveFriend = async (friendId) => {
  try {
    const response = await axiosClient.post("/friends/remove", {
      friendId,
    });
    console.log("post removefriendRequest Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "删除好友失败",
      type: "error",
    });
    console.error("Failed to remove friend", error);
    throw error;
  }
};

// 获取朋友圈动态√
const getFriendCircle = async (page, pageSize) => {
  try {
    const response = await axiosClient.get("/feed", {
      params: { page, pageSize },
    });
    return response;
  } catch (error) {
    ElNotification({
      message: "获取好友圈列表失败",
      type: "error",
    });
    console.error("Failed to get friend circle", error);
    return null;
  }
};

// 获取好友申请列表√
const getFriendRequestList = async () => {
  try {
    const response = await axiosClient.get("/friends/requestlist");
    console.log("get friendRequestList Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取好友申请列表失败",
      type: "error",
    });
    console.error("Failed to get friend request list", error);
    return null;
  }
};

// 发布帖子
const postMessage = async (content) => {
  try {
    const response = await axiosClient.post("/feed/post", {
      content,
    });
    console.log("post message Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "发布帖子失败",
      type: "error",
    });
    console.error("Failed to post message", error);
    throw error;
  }
};

// 点赞帖子
const postThumbsUp = async (feed_id) => {
  try {
    const response = await axiosClient.post("/feed/like", { feed_id });
    console.log("post thumbsUp Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "点赞帖子失败",
      type: "error",
    });
    console.error("Failed to thumbsUp message", error);
    throw error;
  }
};

// 取消点赞
const deleteThumbsUp = async (feedId) => {
  try {
    const response = await axiosClient.delete(`/feed/${feedId}/like`);
    console.log("delete thumbsUp Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "取消点赞失败",
      type: "error",
    });
    console.error("Failed to delete thumbsUp", error);
    throw error;
  }
};

// 评论帖子
const postComment = async (feedId, content) => {
  try {
    const response = await axiosClient.post("/feed/comment", {
      feedId,
      content,
    });
    console.log("post comment Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "评论帖子失败",
      type: "error",
    });
    console.error("Failed to comment message", error);
    throw error;
  }
};

// 获取评论
const getComments = async (feedId) => {
  try {
    const response = await axiosClient.get("/feed/comments", {
      params: { feedId },
    });
    console.log("get commentList Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取评论失败",
      type: "error",
    });
    console.error("Failed to get commentList", error);
    return null;
  }
};

// 获取任务排行榜
const getTaskRankList = async (period) => {
  try {
    const response = await axiosClient.get("/analysis/leaderboard/task", {
      params: { period },
    });
    console.log("get TaskRankList Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取目标排行榜失败",
      type: "error",
    });
    console.error("Failed to get TaskRankList", error);
    return null;
  }
};

// 获取目标排行榜
const getObjRankList = async (period) => {
  try {
    const response = await axiosClient.get("/analysis/leaderboard/objective", {
      params: { period },
    });
    console.log("get ObjRankList Successfully", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取任务排行榜失败",
      type: "error",
    });
    return null;
  }
  };


  //获取好友界面-好友完成情况
  const getObjHubStatus = async (userId) => {
    try {
      const response = await axiosClient.get(`/analysis/friend/${userId}/objhubstatus`);
      return response;
    } catch (error) {
      ElNotification({
        message: "获取好友目标完成情况失败",
        type: "error",
      });
      return null;
    }
  };

const pushLocation = async (latitude, longitude) => {
  try {
    console.log("push location",latitude,longitude)
    const response = await axiosClient.put("/user/location", {
      latitude,longitude
    })
    console.log("push location Successfully", response);
  }catch (error) {
    console.error("Failed to push location", error);
    ElNotification({
      message: "上传用户位置失败",
      type: "error",
    });
  }
}

const getPeerNearby = async () => {
  try {
    const response = await axiosClient.get("/friends/nearby",{});
    console.log("get nearby Successfully", response);

    if(response==null){
      ElNotification({
        message: "附近暂时没有用户",
        type: "info",
      });
    }
    return response;
  }catch (error) {
    ElNotification({
      message: "获取附近的人失败",
      type: "error",
    });
 }
};


const getRecentFeed = async () => {
  try {
    const response = await axiosClient.get("/feed/recent",{});
    console.log("get recent feed Successfully", response);
    return response;
  }catch (error) {
    ElNotification({
      message: "获取动态推荐",
      type: "error",
    });
 }

};

export default {
  getFriends,
  getFriendProfile,
  postAddFriend,
  searchUsers,
  postAcceptFriend,
  postRemoveFriend,
  getFriendCircle,
  getFriendRequestList,
  postMessage,
  postThumbsUp,
  deleteThumbsUp,
  postComment,
  getComments,
  getTaskRankList,
  getObjRankList,
  loadFriends,
  getObjHubStatus,
  pushLocation,
  getPeerNearby,
  getRecentFeed,
};
