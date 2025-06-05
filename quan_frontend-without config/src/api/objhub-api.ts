import { axiosClient } from "./axios-api";
import { ElNotification } from "element-plus";
import { getUserInfo } from "./userinfo-api";
import store from "@/data/vuex-data";

interface ObjectiveResponse {
  id: number;
  name: string;
  children: any[];
  tasks: any[];
}

const getUserObjectives = async (userId) => {
  try {
    const response = await axiosClient.get<ObjectiveResponse>(`/objectives/user/${userId}`);
    console.log("user objs:", response);
    store.commit("setObjList", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "初始化数据失败",
      type: "error",
    });
    console.error("Error get user objs", error);
    return null;
  }
};

const getPeerObjectives = async (userId) => {
  try {
    const response = await axiosClient.get<ObjectiveResponse>(`/objectives/user/${userId}`);
    console.log("peer objs:", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取好友近期目标失效",
      type: "error",
    });
    console.error("Error get user objs", error);
    return null;
  }
};

const getSingleObjectiveDetails = async (ObjId) => {
  try {
    const response = await axiosClient.get(`/objectives/${ObjId}`);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取当前目标详情失败",
      type: "error",
    });
    console.error("Error get ", error);
    return null;
  }
};

const getUserTodayTasks = async (userId) => {
  try {
    const response = await axiosClient.get(`/tasks/user/${userId}/today`);
    console.log("user today tasks:", response);
    store.commit("setTodayTaskList", response);
    return response;
  } catch (error) {
    ElNotification({
      message: "获取用户今日任务失败",
      type: "error",
    })
  }
}

const postObj = async (objData) => {
  try {
    const response = await axiosClient.post("/objectives", objData);
    console.log("Obj created successfully:", response);

    getUserObjectives(getUserInfo().id);

    return response;
  } catch (error) {
    ElNotification({
      message: "创建目标失败",
      type: "error",
    });
    console.error("Error creating obj:", error.response?.data || error.message);
    throw error;
  }
};

const getSingleTaskDetails = async (taskId) => {
  try {
    const response = await axiosClient.get(`/tasks/${taskId}`);
    return response;
  }catch (error) {
    ElNotification({
      message: "获取任务详情失败",
      type: "error",
    })
  }
}

const postSingleTask = async (taskData, objId) => {
  try {
    const response = await axiosClient.post("/tasks/single", taskData);
    console.log("SingleTask created successfully:", response);
    getSingleObjectiveDetails(objId);
    return response;
  } catch (error) {
    ElNotification({
      message: "创建单次任务失败",
      type: "error",
    });
    console.error(
      "Error creating task:",
      error.response?.data || error.message
    );
    throw error;
  }
};

const postRecurringTask = async (taskData, objId) => {
  try {
    const response = await axiosClient.post("/tasks/recurring", taskData);
    console.log("RecurringTask created successfully:", response);
    getSingleObjectiveDetails(objId);
    return response;
  } catch (error) {
    ElNotification({
      message: "创建周期任务失败",
      type: "error",
    });
    console.error(
      "Error creating task:",
      error.response?.data || error.message
    );
    throw error;
  }
};

const deleteObj = async (objId) => {
  try {
    const response = await axiosClient.delete(`/objectives/${objId}`);
    console.log("delete obj successfully", objId);
    getUserObjectives(getUserInfo().id)
    return response;
  } catch (error) {
    ElNotification({
      message: "删除目标失败",
      type: "error",
    });
    console.error("Error deleting obj:", error.response?.data || error.message);
    throw error;
  }
};

const deleteTask = async (taskId) => {
  origin = taskId;
  try {
    const response = await axiosClient.delete(`/tasks/${taskId}`);
    console.log("delete task successfully", origin);
    return response;
  } catch (error) {
    ElNotification({
      message: "删除目标失败",
      type: "error",
    });
    throw error;
  }
};

const createChildObj = async (parentId,data) => {
  try {
    const response = await axiosClient.post(`/objectives/parent/${parentId}/children`, data);
    console.log("create child objsuccessfully", data);
  }catch (error) {
    ElNotification({
      message: "创建子目标失败",
      type: "error",
    });
  }
}
const associateObj = async (fatherId, data) => {
  try {
    origin = fatherId;
    const response = await axiosClient.post(
      `/objectives/${fatherId}/children`,
      data
    );
    console.log(
      "associate child " +
      data.childObjectiveId +
      " and father " +
      origin +
      " successfully"
    );
    return response;
  } catch (error) {
    ElNotification({
      message: "关联目标失败",
      type: "error",
    });
    console.error(
      "Error associating obj and task:",
      error.response?.data || error.message
    );
    throw error;
  }
};

const updateObj = async (objId, data) => {
  try {
    origin = objId;
    const response = await axiosClient.put(`/objectives/${objId}`, data);
    return response;
  } catch (error) {
    ElNotification({
      message: "修改目标失败",
      type: "error",
    });
    console.error("Error changing obj:", error.response?.data || error.message);
    throw error;
  }
};

const updateSingleTask = async (taskId, data) => {
  try {
    origin = taskId;
    const response = await axiosClient.put(`/tasks/single/${taskId}`, data);
    console.log("change single task " + origin + " successfully");
    return response;
  } catch (error) {
    ElNotification({
      message: "修改单次任务失败",
      type: "error",
    });
    console.error(
      "Error changing single task:",
      error.response?.data || error.message
    );
    throw error;
  }
};

const updateRecurringTask = async (taskId, data) => {
  try {
    origin = taskId;
    const response = await axiosClient.put(`/tasks/recurring/${taskId}`, data);
    console.log("change recurring task " + origin + " successfully");
    return response;
  } catch (error) {
    ElNotification({
      message: "修改周期任务失败",
      type: "error",
    });
    console.error(
      "Error changing recrring task:",
      error.response?.data || error.message
    );
    throw error;
  }
};

const completeTask = async (data) => {
  try {
    const response = await axiosClient.post('/tasks/completion', data);
    console.log("complete task " + data.id + " successfully");
    return response;
  } catch (error) {
    ElNotification({
      message: "完成任务失败",
      type: "error",
    })
  }
}

const cancelCompleteTask = async (taskId) => {
  try {
    const response = await axiosClient.delete(`/tasks/completion/${taskId}`);
    console.log("cancel complete task " + taskId + " successfully");
    return response;
  } catch (error) {
    ElNotification({
      message: "取消完成任务失败",
      type: "error",
    })
  }
}

const getAiGuidanceForObjective = async (objectiveId) => {
  try {
    const response = await axiosClient.get(`/objectives/${objectiveId}/ai-guide`);
    return response;
  } catch (error) {
    ElNotification({ 
      message: "取消完成任务失败",
      type: "error",
     })
  }
}

export default {
  getUserObjectives,
  getPeerObjectives,
  getSingleObjectiveDetails,
  getUserTodayTasks,
  postObj,
  postSingleTask,
  postRecurringTask,
  deleteObj,
  deleteTask,
  associateObj,
  updateObj,
  updateSingleTask,
  updateRecurringTask,
  completeTask,
  cancelCompleteTask,
  getAiGuidanceForObjective,
  getSingleTaskDetails,
  createChildObj
};
