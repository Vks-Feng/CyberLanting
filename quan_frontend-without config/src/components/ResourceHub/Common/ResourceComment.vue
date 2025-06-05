<template>
  <div class="resource-comment">
    <h3>评论</h3>
    <div class="comment-input">
      <el-input v-model="newComment" placeholder="写下你的评论..." />
      <el-button class="ink-button" type="primary" @click="postComment">发布</el-button>
    </div>
    
    <ul class="comment-list">
      <li v-for="comment in comments" :key="comment.id" class="comment-item">
        <img :src="comment.userVO.avatarUrl" alt="头像" class="avatar" />
        <div class="comment-content">
          <div class="comment-header">
            <span class="nickname">{{ comment.userVO.nickname }}</span>
            <span class="timestamp">{{ formatDate(comment.createdAt) }}</span>
          </div>
          <p class="comment-text">{{ comment.content }}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted, watch } from 'vue';
import { ElNotification } from 'element-plus';
import resourceCenter from '@/api/resource-center';
import store from '@/data/vuex-data';
import dayjs from 'dayjs';
import { getUserInfo } from '@/api/userinfo-api';

const props = defineProps({ resourceId: Number });
const comments = ref([]);
const newComment = ref('');

const loadComments = async () => {
  const data = await resourceCenter.getResourceComments(props.resourceId);
  comments.value = data;
};

onMounted(async () => {
  await loadComments();
});

watch(
  () => props.resourceId,
  async (newId) => {
    if (!newId) return;
    try {
      comments.value = [];
      await loadComments();
    } catch (error) {
      ElNotification({ message: '加载资源失败', type: 'error' });
    }
  },
  { immediate: true }
);

const postComment = async () => {
  if (!newComment.value) return;
  await resourceCenter.commentOnResource(props.resourceId, { content: newComment.value });
  comments.value.push({
    id: Date.now(),
    userVO: {
      nickname: getUserInfo().nickname, 
      avatarUrl: getUserInfo().avatarUrl,
    },
    content: newComment.value,
    createdAt: new Date().toISOString(),
  });
  newComment.value = '';
};

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm');
};
</script>

<style scoped>
.resource-comment {
  margin-top: 20px;
}
.comment-input {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}
.comment-list {
  list-style: none;
  padding: 0;
}
.comment-item {
  display: flex;
  align-items: flex-start;
  padding: 10px;
  border-bottom: 1px solid #eaeaea;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}
.comment-content {
  flex: 1;
}
.comment-header {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #666;
}
.nickname {
  font-weight: bold;
  color: #333;
}
.timestamp {
  font-size: 12px;
}
.comment-text {
  margin: 5px 0 0;
  font-size: 14px;
}
</style>
