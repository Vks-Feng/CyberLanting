import { ElNotification } from "element-plus";
import { axiosClient } from "./axios-api";
import { getUserInfo } from "./userinfo-api";

const getHeatmapData = async (year) => {
  try {
    const response = await axiosClient.get("/analysis/heatmap/tasks", {
      params: {
        // userId: localStorage.getItem('userId'),//从vuex中获取userId
        // userId: 1,
         userId: getUserInfo().id,
        year,
      },
    });
     console.log("获取热力图数据成功:", response); // 检查完整的响应数据结构

    return response;
  } catch (error) {
    ElNotification({
      message: "获取数据失败",
      type: "error",
    });
    console.error("获取热力图数据失败:", error);
    return null;
  }
};

export default {
  getHeatmapData,
};
