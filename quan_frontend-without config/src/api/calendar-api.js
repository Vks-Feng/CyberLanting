import { ElNotification } from "element-plus";
import { axiosClient } from "./axios-api";
import { getUserInfo } from "./userinfo-api";

const getCalendarTasks = async (year, month) => {
  try {
    const response = await axiosClient.get("/analysis/calendar/tasks/month", {
      params: {
        userId: getUserInfo().id,
        // userId: 1,
        year,
        month,
      },
    });

    // console.log("获取日历任务数据成功:", response);
    return response;
    
  } catch (error) {
    ElNotification({
      message: "获取日历任务数据失败",
      type: "error",
    });
    console.error("获取日历任务数据失败:", error);
    return [];
  }
};

export default {
  getCalendarTasks,
};
