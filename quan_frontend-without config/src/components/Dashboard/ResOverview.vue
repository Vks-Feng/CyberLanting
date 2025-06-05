<template>
  <div class="peer-feed-container">
    <h2 class="title">📚 资源推荐</h2>
    <el-scrollbar class="feed-scrollbar" v-if="ResList.length > 0">
      <ol class="feed-list">
        <li v-for="item in ResList" :key="item.id" class="feed-item">
          <span class="username">{{ item.userVO?.username || '匿名用户' }}</span>：
          <span class="content">
            <strong>{{ item.title }}</strong> - {{ item.description }}
          </span>
        </li>
      </ol>
    </el-scrollbar>
    <p v-else>暂无推荐资源</p> <!-- 如果没有数据，显示此内容 -->
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import resourceApi from "@/api/resource-center";

const ResList = ref([]);

onMounted(async () => {
  try {
    const res = await resourceApi.getRecommendResources();
    ResList.value = res.list || []; // 处理可能的空数据
  } catch (error) {
    console.log(error);
  }
});
</script>

<style scoped>
.peer-feed-container {
  height: 100%;
  width: 100%;
  max-height: 100%;
  max-width: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.feed-scrollbar {
  flex: 1;
  overflow: auto;
  max-height: 100%;
}

.feed-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.title {
  font-size: 24px;
  font-weight: bold;
  border-bottom: 1px solid #ddd;
  margin: 0px 0px 10px 0px;
}

.feed-item {
  padding: 10px;
  font-size: 18px;
  transition: background-color 0.3s ease;
  border-bottom: 1px dashed #ccc;
}

.feed-item:hover {
  background-color: #f0f0eb;
}

.username {
  width: 30%;
  font-weight: bold;
  color: #4a4a4a;
}

.content {
  width: 70%;
  margin-left: 4px;
  color: #555;
}

p {
  color: #999;
  text-align: center;
  font-size: 18px;
  padding: 20px;
}
</style>
