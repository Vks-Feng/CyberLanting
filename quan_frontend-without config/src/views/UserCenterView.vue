<template>
  <el-scrollbar>
    <!-- 返回主页按钮 -->
    <div class="fixed-back-btn-container">
      <el-button class="fixed-back-btn" @click="onBack"
        ><el-icon><ArrowLeftBold /></el-icon
      ></el-button>
    </div>

    <!-- 编辑信息 -->
    <EditUserDialog
      v-model="editDialogVisible"
      :form="editForm"
      @close="editDialogVisible = false"
      @refresh="UserInfoCardId++"
    />

    <div class="profile-container">

      <!-- 用户信息卡片 -->
      <UserinfoCard :key="UserInfoCardId" @edit="handleEdit" />

      <!-- 主要内容 -->
      <div class="profile-content">

        <!-- 左侧信息 -->
        <div class="profile-sidebar">

          <UserActivityCard />
          <UserSocialAchievement />
          <UserCalendar />

        </div>

        <!-- 右侧内容区域 -->
        <div class="profile-main">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="志鉴" name="analysis">
              <UserAnalysis />
            </el-tab-pane>
            <!-- <el-tab-pane label="收藏" name="repositories">
            <p>暂无收藏</p>
          </el-tab-pane> -->
            <el-tab-pane label="经阁" name="resources">
              <UserResourceList />
            </el-tab-pane>

            <el-tab-pane label="迹途" name="achievement">
              <UserAchievement />
            </el-tab-pane>

          </el-tabs>
        </div>
      </div>
    </div>
  </el-scrollbar>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getUserInfo, updateUserInfo } from "@/api/userinfo-api";
import { useRouter } from "vue-router";
import Heatmap from "@/components/Analysis/Heatmap.vue";
import UserResourceList from "@/components/Common/UserResourceList.vue";
import analysisApi from "@/api/analysis-api";
import EditUserDialog from "@/components/UserCenter/EditUserDialog.vue";
import UserinfoCard from "@/components/UserCenter/UserInfoCard.vue";
import UserActivityCard from "@/components/UserCenter/UserActivityCard.vue";
import UserSocialAchievement from "@/components/UserCenter/UserSocialAchievement.vue";
import UserAchievement from "@/components/UserCenter/UserAchievement.vue";
import UserAnalysis from "@/components/UserCenter/UserAnalysis.vue";
import UserCalendar from "@/components/UserCenter/UserCalendar.vue";

const activeTab = ref("analysis");
var UserInfoCardId = ref(0);

// 模拟数据

const router = useRouter();
const editDialogVisible = ref(false);

//编辑信息表单
const editForm = ref({
  nickname: getUserInfo().nickname,
  email: getUserInfo().email,
  avatarUrl: getUserInfo().avatarUrl,
});

//获取card数据
onMounted(async () => {

});

const handleEdit = () => {
  editDialogVisible.value = true;
};

// 返回主页
const onBack = () => {
  router.push({ name: "home" });
};
</script>

<style scoped>
.fixed-back-btn-container {
  height: 20px;
}
/* 整体容器 */
.profile-container {
  width: 100%;
  background: #f4f5f7;
  background: url("/images/user-bg.png") center;
  background-size: cover;
}

/* 主要内容区域 */
.profile-content {
  display: flex;
  width: 80%;
  margin: 20px auto;
}

/* 侧边栏 */
.profile-sidebar {
  width: 25%;
  margin-right: 20px;
}

/* 个人信息卡片 */
.info-card {
  padding: 15px;
  border-radius: 10px;
  /* background: white; */
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 右侧主要内容 */
.profile-main {
  flex: 1;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

}

/* 按钮 */
.fixed-back-btn {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 10;
}

:deep(.el-tabs__item) {
  font-size: 20px;
}

:deep(.el-tabs__item.is-active) {
  color: #607B8B !important;
}

:deep(.el-tabs__active-bar) {
  background-color: #607B8B !important;
}
:deep(.el-tabs__item:hover) {
  color: #607B8B;
}
</style>
