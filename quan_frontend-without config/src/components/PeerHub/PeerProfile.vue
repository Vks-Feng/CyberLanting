<template>
  <el-divider />

  <div class="friend-profile">
    <el-scrollbar>
    <!-- 顶部信息卡片 -->
    <el-card class="profile-card">
      <div class="profile-header">
        <div class="avatar-wrapper">
          <el-avatar :size="100" :src="friend.avatarUrl" />
        </div>
        <div class="profile-info">
          <h2>{{ friend.nickname || "好友" }}</h2>
          <h3>{{ friend.email || "未绑定邮箱" }}</h3>
        </div>
      </div>
    </el-card>

    <!-- 任务统计 -->
    <div class="task-stats">
      <PeerCompletionStatistics :friendId="friend.id" />
    </div>

    <!-- 最近任务 -->
    <el-card class="recent-tasks">
      <template #header>
        <span>今日任务</span>
        <PeerTodayTask :id="friend.id" />
      </template>
    </el-card>

    <el-card class="recent-tasks">
      <template #header>
        <span>近期目标</span>
        <PeerRecentObjs :id="friend.id" />
      </template>
    </el-card>


    <div class="box-container"></div> 
  </el-scrollbar>

  </div>

  <el-divider />

</template>

<script>
import peerApi from "@/api/peer-api";
import { ElNotification } from "element-plus";
import { ref, watch } from "vue";
import PeerTodayTask from "./PeerTodayTask.vue";
import PeerCompletionStatistics from "./PeerCompletionStatistics.vue";
import PeerRecentObjs from "./PeerRecentObjs.vue";

export default {
  props: {
    friend: {
      type: Object,
    },
  },
  components: {
    PeerTodayTask,
    PeerCompletionStatistics,
    PeerRecentObjs,
  },
  setup(props) {


    //需要设计成响应式变量，可以根据好友的状态实时更新
    //不能直接设为空数组，再用value赋值！因为普通数组根本没有value属性
    const friendStatus = ref({});

    const getFriendObjStatus = async () => {
      try {
        console.log("好友id", props.friend.id);
        const response = await peerApi.getObjHubStatus(props.friend.id);
        friendStatus.value = response;

      } catch (error) {
        ElNotification({
          message: "获取好友状态失败",
          type: "error",
        });
        console.log(error);
      }
    };



    // 监听 `friend` 的变化，可以根据需求去重新处理或刷新数据
    watch(
      () => props.friend,
      (newFriend) => {
        // 友好地处理切换好友的情况
        console.log("切换好友", newFriend);
        getFriendObjStatus();
      },
      { immediate: true }
    );


    return {

      getFriendObjStatus,
    };
  },




};
</script>

<style scoped>
.box-container {  
  height: 50px;
  background: transparent;
}

.friend-profile {
  height: 670px;
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.profile-card {
  padding: 40px 20px 0px;
  position: relative;
  text-align: center;
}

.avatar-wrapper {
  position: absolute;
  top: 5px;
  left: 50%;
  transform: translateX(-50%);
}

.profile-info h2 {
  margin-top: 50px;
  margin-bottom: 10px;
  font-size: 18px;
  font-weight: bold;
}

.profile-info h3 {
  font-size: 14px;
  color: gray;
}

.recent-tasks {
  margin-top: 15px;

}

.task-item h4 {
  margin: 0;
  font-size: 14px;
}

.task-item p {
  font-size: 12px;
  color: gray;
}
</style>
