<template>
  <div class="rank-container">
    <h2 class="title">🏆 任务完成数月榜</h2>
    <el-scrollbar height="100%">
      <ol class="rank-list">
        <li
          v-for="(user, index) in rankList"
          :key="user.userId"
          class="rank-item"
          :class="getRankClass(index)"
        >
          <div class="rank-left">
            <el-avatar :src="user.avatarUrl" :size="36" />
            <span class="username">{{ user.username }}</span>
          </div>
          <div class="rank-right">
             {{ user.completeCount }}
          </div>
        </li>
      </ol>
    </el-scrollbar>
  </div>
</template>



<script lang="ts" setup>
import { onMounted, ref } from "vue";
import peerApi from "@/api/peer-api";
import { ElAvatar } from "element-plus";

const rankList = ref([]);

onMounted(async () => {
  try {
    rankList.value = await peerApi.getTaskRankList("monthly");
    console.log(rankList.value);
  } catch (error) {
    console.log(error);
  }
});

const getRankClass = (index: number) => {
  if (index === 0) return "gold";
  if (index === 1) return "silver";
  if (index === 2) return "bronze";
  return "";
};

</script>

<style scoped>
.rank-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 16px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding-bottom: 8px;
}

.rank-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.rank-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  margin-bottom: 8px;
  color: #222;
  transition: background-color 0.3s ease;
}

.rank-item.gold {
  background: rgba(255, 215, 0, 0.15);
}

.rank-item.silver {
  background: rgba(192, 192, 192, 0.15);
}

.rank-item.bronze {
  background: rgba(205, 127, 50, 0.15);
}

.rank-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: large;
}

.username {
  font-weight: 500;
  color: #333;
  font-size: 24px;
}

.rank-right {
  font-family: 'Times New Roman', serif;
  min-width: 80px;
  text-align: right;
  color: #444;
}

</style>
