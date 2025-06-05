<template>
  <el-card class="peer-request-card" shadow="never">
    <template #header>
      <div class="header">
        <span>好友请求</span>
      </div>
    </template>
    <el-empty v-if="noRequest" description="暂无好友请求" />
    <div v-else>
      <div
        v-for="request in requests"
        :key="request.id"
        class="request-item"
      >
        <el-avatar :src="request.avatarUrl" class="avatar" />
        <div class="request-info">
          <span class="username">{{ request.username }}</span>
        </div>
        <div class="actions">
          <el-button
            type="success"
            size="small"
            @click="acceptRequest(request.id)"
            circle
          >
            <el-icon style="color: black;"><Check /></el-icon>
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="rejectRequest(request.id)"
            circle
          >
            <el-icon style="color: black;"><Close /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ElNotification } from "element-plus";
import { Check, Close } from "@element-plus/icons-vue";
import peerApi from "@/api/peer-api";
import { getUserInfo } from "@/api/userinfo-api";

const requests = ref([]);
const noRequest = ref(false);
const emit = defineEmits(["friendAccepted"]);

const fetchRequests = async () => {
  try {
    const response = await peerApi.getFriendRequestList();
    console.log("获取好友请求成功");
    if (response.length === 0) {
      noRequest.value = true;
      requests.value = [];
      console.log("暂无好友请求");
    } else {
      requests.value = response;
      console.log("好友请求",requests.value);
    }
  } catch (error) {
    ElNotification({
      message: "获取好友请求失败",
      type: "error",
    });
    console.error(error);
  }
};


//未实现: 点击同意时同步更新好友列表
const acceptRequest = async (Id) => {
  try {
    console.log("接受好友请求：",Id);
    await peerApi.postAcceptFriend(Id);
    requests.value = requests.value.filter((request) => request.id !== getUserInfo().id);

    //刷新请求列表
    await fetchRequests();

    //刷新好友列表
    emit("friendAccepted");

    ElNotification({
      message: "好友请求已接受",
      type: "success",
    });
    console.log("接受好友成功");
  
  } catch (error) {
    ElNotification({
      message: "接受好友请求失败",
      type: "error",
    });
    console.log("接受好友失败：",error);
  }
};

const rejectRequest = async (Id) => {
  try {
    await peerApi.postRemoveFriend(Id);
    requests.value = requests.value.filter((request) => request.id !== getUserInfo().id);

    await fetchRequests();
    
    ElNotification({
      message: "好友请求已拒绝",
      type: "success",
    });
  } catch (error) {
    ElNotification({
      message: "拒绝好友请求失败",
      type: "error",
    });
  }
};

onMounted(fetchRequests);
</script>

<style scoped>
.peer-request-card {
  /* width: 350px; */
  border: none;
}
.header {
  font-size: 16px;
  font-weight: bold;
}
.request-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px soluserId #ebeef5;
}
.request-item:last-child {
  border-bottom: none;
}
.avatar {
  flex-shrink: 0;
}
.request-info {
  flex-grow: 1;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
}
.username {
  font-weight: bold;
}
.message {
  font-size: 12px;
  color: gray;
}
.actions {
  display: flex;
  gap: 5px;
}
</style>
