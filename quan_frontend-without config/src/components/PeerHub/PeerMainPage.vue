<template>
  <div class="commom-layout">
    <!-- 好友列表 -->
    <el-container>
      <el-aside width="25%" class="peer-bar">
        <h2>墨络轩</h2>
        <el-row>
          <el-col>
            <div class="btn-group">
              <!-- 添加好友/朋友圈/排行榜 -->

              <el-tooltip content="添加好友" >
              <el-button @click="showAddFriendDialog = true"><el-icon>
                  <Plus />
                </el-icon></el-button>
              </el-tooltip>

              <el-tooltip content="好友圈">
              <el-button @click="currentView = PeerCircle"><el-icon>
                  <Share />
                </el-icon></el-button>
              </el-tooltip>

              <!-- 省略 -->
              <!-- <el-tooltip content="排行榜">
              <el-button @click="currentView = PeerRank"><el-icon>
                  <TrophyBase />
                </el-icon></el-button>
              </el-tooltip> -->

              <el-tooltip content="好友申请">
              <el-button @click="currentView = PeerRequest"><el-icon>
                  <Message />
                </el-icon></el-button>
              </el-tooltip>

              <el-tooltip content="附近的人">
              <el-button @click="currentView = PeerNearby"><el-icon>
                  <MapLocation />
                </el-icon></el-button>
              </el-tooltip>
            </div>
          </el-col>

          <el-divider style="color: white; margin : 20px 5px 5px 5px"/>

          <!-- 好友列表 -->
          <el-col :span="12" style="display: contents">
            <h5>好友列表</h5>
            <el-menu default-active="1" class="friend-menu">
              <el-menu-item v-for="friend in filteredList" :key="friend.id" :index="String(friend.id)"
                class="friend-item" @click="handleSelect(friend.id)">

                <div style="padding-left: 10px;display: flex; align-items: center;">
                  <el-avatar :size="40" :src="friend.avatarUrl" />
                  <span class="friend-name">{{ friend.nickname }}</span>
                </div>
                <!-- <el-button
                  class="remove-btn"
                  @click.stop="handleRemoveFriend(friend.id)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button> -->

                <el-icon style="padding-right:20px" @click.stop="handleRemoveFriend(friend.id)" size="35">
                  <Delete />
                </el-icon>
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-aside>

    

      <el-container>

        <el-main>
          <!-- 动态组件渲染 -->
          <component :is="currentView" :friend="selectFriend" @friendAccepted="handleFriendAccept"
            @selectFriend="handleSelect" />
        </el-main>
        <!-- 
        <el-divider /> -->

        <!-- 好友申请弹窗
        <el-dialog v-model="showAddFriendDialog" title="添加朋友" width="30%">
          <el-form :model="addFriendForm" label-width="80px">
            <el-form-item label="用户ID">
              <el-input v-model="addFriendForm.userId" placeholder="请输入用户ID" />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="showAddFriendDialog = false">取消</el-button>
              <el-button type="primary" @click="addFriend">添加</el-button>
            </span>
          </template>
        </el-dialog> -->

            <!-- 添加好友弹窗 -->
        <AddFriendDialog
          v-model:visible="showAddFriendDialog"
        />

        <el-footer></el-footer>
      </el-container>
    </el-container>
  </div>

</template>

<script lang="ts" setup>
import { ref, computed, shallowRef, onMounted } from "vue";
import peerApi from "@/api/peer-api";
import { ElNotification } from "element-plus";
import PeerCircle from "./PeerCircle.vue";
import PeerRank from "./PeerRank.vue";
import PeerRequest from "./PeerRequest.vue";
import { getUserInfo } from "@/api/userinfo-api";
import PeerProfile from "./PeerProfile.vue";
import AddFriendDialog from "./AddFriendDialog.vue";
import PeerNearby from "./PeerNearby.vue";
import { LocateIcon } from "lucide-vue-next";

// 好友列表
const friendList = ref([]);
const selectFriend = ref(null);
const searchQuery = ref("");
const showAddFriendDialog = ref(false);
const currentView = shallowRef(PeerCircle); //避免深度响应化组件
const addFriendForm = ref({
  userId: "",
});


//添加好友处理
const addFriend = async () => {
  try {
    if (addFriendForm.value.userId === getUserInfo().id) {
      ElNotification({
        message: "不能添加自己为好友",
        type: "error",
      });
      return;
    } else {
      const response = await peerApi.postAddFriend(addFriendForm.value.userId);
      ElNotification({
        message: "发送好友申请成功",
        type: "success",
      });

      addFriendForm.value.userId = "";
      showAddFriendDialog.value = false;
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

// 过滤好友列表
const filteredList = computed(() => {
  if (!searchQuery.value) return friendList.value;

  return friendList.value.filter((friend) =>
    friend.nickname.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 好友列表
const fetchFriends = async () => {
  try {
    friendList.value = await peerApi.loadFriends();
  } catch (error) {
    ElNotification({
      message: "获取好友列表失败",
      type: "error",
    });
  }
};

//刷新好友列表
const refreshFriends = async () => {
  try {
    await fetchFriends();
  } catch (error) {
    ElNotification({
      message: "刷新好友列表失败",
      type: "error",
    });
  }
};

// 通过组件通信实现列表同步更新
const handleFriendAccept = async () => {
  refreshFriends();
};

//选择好友
const handleSelect = async (friendId: number) => {
  const friend = friendList.value.find((f) => f.id === friendId);
  if (friend) {
    selectFriend.value = friend;
  }
  currentView.value = PeerProfile;
};

//移除好友
const handleRemoveFriend = async (friendId: number) => {
  try {
    await peerApi.postRemoveFriend(friendId);
    await refreshFriends();
    ElNotification({
      message: "移除好友成功",
      type: "success",
    });
  } catch (error) {
    ElNotification({
      message: "移除好友失败",
      type: "error",
    });
    console.error("移除好友失败", error);
  }
};

onMounted(() => {
  fetchFriends();
});
</script>

<style scoped>
.friend-menu {
  width: 100%;
  border-right: none;
  border-radius: 3px;
}

.friend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  transition: background 0.3s;
}

.friend-item:hover {
  background: #f5f7fa;
}

.friend-item.is-active {
  background: rgba(17, 37, 48, 0.2);

}


.friend-name {
  flex-grow: 1;
  margin-left: 10px;
  font-size: 14px;
  color: #333;
}

.remove-btn {
  border: none;
  background: none;
  padding: 0;
  color: #f56c6c;
}

.remove-btn:hover {
  background: rgba(245, 108, 108, 0.1);
}


.btn-group {
  display: flex;
  justify-content: space-between;
  margin-right: 20px;
  margin-left: 20px;
  /* gap: 0px; */
}
</style>
