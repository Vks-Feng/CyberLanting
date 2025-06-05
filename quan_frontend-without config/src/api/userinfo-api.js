import store from "@/data/vuex-data";
import { axiosClient } from "./axios-api";

const getUserInfo = () => {
  // 获取本地信息
  const info = localStorage.getItem("userInfos");
  var userInfo = null;
  if (info) {
    userInfo = JSON.parse(info);
  } else {
    console.error("No user info found in local storage");
  }
  return userInfo;
};

const updateUserInfo = async (nickname, avatar) => {
  //Step1. 进行数据库更新
  var response;
  try {
    const data = {
      id: getUserInfo().id,
      nickname: nickname,
      avatarUrl: avatar,
    };
    response = await axiosClient.put("/user", data);
    console.log("Updated user info Successfully");
  } catch (error) {
    ElNotification.error({
      title: "更新失败",
      message: error,
    });
    throw error;
  }

  //Step2. 进行本地存储量的更新
  const stored = localStorage.getItem("userInfos");
  if (!stored) {
    console.error("No userInfos found in localStorage.");
    return;
  }

  let userInfo;
  try {
    userInfo = JSON.parse(stored);
  } catch (e) {
    console.error("Failed to parse userInfos from localStorage:", e);
    return;
  }

  // 更新 nickname 和 avatarUrl，保留其他字段
  const updatedUserInfo = {
    id: userInfo.id,
    email: userInfo.email,
    nickname: nickname,
    avatarUrl: avatar,
  };

  // 写回 localStorage
  localStorage.setItem("userInfos", JSON.stringify(updatedUserInfo));
  return response;
};

const getUserAchievements = async () => {
  try {
    const response = await axiosClient.get("/achievement");
    return response;
  } catch (error) {
    console.error("Failed to get user achievements:", error);
    throw error;
  }
};

export { getUserInfo, updateUserInfo, getUserAchievements };
