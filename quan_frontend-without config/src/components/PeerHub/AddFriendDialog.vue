<template>
    <el-dialog v-model="show" title="添加朋友" width="30%">
      <el-form label-width="80px" class="form-container">
        <el-form-item label="用户ID">
          <el-input v-model="searchId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-button class="ink-button" type="primary" @click="searchUser">搜索</el-button>
      </el-form>
  
      <!-- 搜索结果展示 -->
      <div v-if="searchedUser" class="user-card">
        <el-card class="card-container">
          <div class="user-info">
            <el-avatar :size="50" :src="searchedUser.avatarUrl" />
            <div class="user-details">
              <p>{{ searchedUser.nickname }} ({{ searchedUser.username }})</p>
              <p>Email: {{ searchedUser.email }}</p>
            </div>
          </div>

          <el-button v-if="!searchedUser.isFriend" type="success" @click="addFriend" class="add-button">
            添加好友
          </el-button>

          <el-tag v-else type="info" class="friend-tag">已是好友/等待对方同意</el-tag>
        </el-card>
      </div>
  
      <template #footer>
      </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, watch, defineProps, defineEmits } from "vue";
import { ElNotification } from "element-plus";
import peerApi from "@/api/peer-api";
import { getUserInfo } from "@/api/userinfo-api";

const props = defineProps({ visible: Boolean });
const emit = defineEmits(["update:visible", "friendAdded"]);

const show = ref(props.visible);
const searchId = ref("");
const searchedUser = ref(null);

// 监听 `visible` 变化，确保 show 状态同步
watch(() => props.visible, (newVal) => {
show.value = newVal;
});

const closeDialog = () => {
show.value = false;
emit("update:visible", false);
searchedUser.value = null;
searchId.value = "";
};

// 搜索用户
const searchUser = async () => {
if (!searchId.value) {
    ElNotification({ message: "请输入用户ID", type: "error" });
    return;
}
try {
    const response = await peerApi.searchUsers(searchId.value);
    console.log("搜索结果", response);
    searchedUser.value = response;
} catch (error) {
    searchedUser.value = null;
    ElNotification({ message: "用户未找到", type: "error" });
}
};

//添加好友处理
const addFriend = async () => {
try {
    if (searchedUser.value.id === getUserInfo().id) {
    ElNotification({
        message: "不能添加自己为好友",
        type: "error",
    });
    return;
    } else {
    const response = await peerApi.postAddFriend(searchedUser.value.id);
    ElNotification({
        message: "发送好友申请成功",
        type: "success",
    });

    console.log("已发送好友申请", response);
    }
} catch (error) {
    ElNotification({
      message: "发送好友申请失败",
      type: "error",
    });
    console.error("申请失败", error);
  }
};
</script>

<style scoped>
/* 水墨风格的背景色与整体色调 */
.form-container {
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.1), rgba(255, 255, 255, 0.3));
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.user-card {
  margin-top: 20px;
  background: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card-container {
  background: #ffffff;
  border: 1px solid #e6e6e6;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-details {
  flex-grow: 1;
}

.add-button {
  background: #b5b5b5;
  color: #fff;
  border-radius: 10px;
  border: none;
  padding: 8px 15px;
  margin-top: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-button:hover {
  background: #a0a0a0;
}

.friend-tag {
  background-color: #e0e0e0;
  color: #333;
  border-radius: 12px;
  padding: 5px 10px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
