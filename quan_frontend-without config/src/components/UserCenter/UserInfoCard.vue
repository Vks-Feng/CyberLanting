<template>
    <!-- 头部背景 -->
    <div class="profile-header">
      <div class="profile-info">
        <!-- 头像 -->
        <el-avatar
          class="profile-avatar"
          :size="100"
          :src="getUserInfo().avatarUrl"
        />
        <!-- 个人信息 -->
        <div class="profile-details">
          <h2 class="profile-name">
            {{ getUserInfo().nickname || "未设置" }}
          </h2>
          <p class="profile-meta">
            <span>ID: {{ getUserInfo().id }}</span> |
            <!-- <span>好友: {{ followers }}</span> | -->
            <span>邮箱: {{ getUserInfo().email }}</span> |
          </p>

          <el-button type="primary" class="ink-button" @click="$emit('edit')">编辑资料</el-button>
        </div>
      </div>
    </div>
</template>


<script setup>
import { getUserInfo } from '@/api/userinfo-api';
</script>

<style scoped>
/* 头部背景 */
.profile-header {
  background-size: cover;
  height: 160px;
  display: flex;
  align-items: flex-end;
  padding: 20px;
}

/* 个人信息 */
.profile-info {
  display: flex;
  align-items: center;
  /* background: white; */
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 100px;
  width: 80%;
  margin: 0 auto;
  /* background-color: transparent !important; */
  backdrop-filter: blur(2px);
  /* 设置磨砂效果的强度 */
  background: rgba(255, 255, 255, 0.3);
  /* 半透明背景 */
  border-radius: 10px;
}

.profile-avatar {
  margin-right: 20px;
}

.profile-details {
  flex: 1;
}

.profile-name {
  font-size: 20px;
  font-weight: bold;

}

.profile-meta {
  color: gray;
  font-size: 14px;
  margin-top: 5px;
}

.edit-btn {
  margin-top: 10px;
  color: gray;

}
</style>