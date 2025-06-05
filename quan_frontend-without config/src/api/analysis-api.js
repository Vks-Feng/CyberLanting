import { ElNotification } from "element-plus";
import { axiosClient } from "./axios-api";


const getLast7daysTaskCompletionRate = async () => {
  try {
    const response = await axiosClient.get("/analysis/completion/task/weekly");
    return response;
  } catch (error) {
    ElNotification({
      message: "获取近7日任务完成率数据失败",
      type: "error",
    });
    console.error("获取日历任务数据失败:", error);
    return [];
  }
};


//个人主页活跃度card
const getUserActivity = async () => {
  try {
    const response = await axiosClient.get("/analysis/activity");
    console.log("活跃度",response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取个人活跃度数据失败",
      type: "error",
    });
    console.error("获取个人活跃度数据失败:", error);
    return [];
  }
};


// 个人主页社交成就card
const getUserSocialAchievement = async () => {
  try {
    const response = await axiosClient.get("/analysis/social/achievement");
    console.log("社交成就",response);
    return response;
    } catch (error) {
      ElNotification({
        message: "获取社交成就数据失败",
        type: "error",
      });
      console.error("获取社交成就数据失败:", error);
      return [];
    }
};

export  default{
    getLast7daysTaskCompletionRate,
    getUserActivity,
    getUserSocialAchievement
};
