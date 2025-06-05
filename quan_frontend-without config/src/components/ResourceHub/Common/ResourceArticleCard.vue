<template>
  <el-card shadow="hover" class="resource-card" @click="goToDetail">
    <div class="content">
      <!-- 用户信息（头像 + 用户名 + 发布时间） -->
      <div class="header">
        <div class="user-info">
          <el-avatar :src="computedData.userVO.avatarUrl" size="default" />
          <span class="username">{{ computedData.userVO.nickname }}</span>
        </div>
        <span class="created-at">{{ formatTime(computedData.createdAt) }}</span>
      </div>

      <!-- 文章标题 + 描述 -->
      <div class="text-content">
        <h4 class="title">{{ computedData.title }}</h4>
        <p class="description">{{ computedData.description }}</p>
      </div>

      <!-- 元信息（点赞、收藏） -->
      <div class="meta-info">
        <span class="meta-item">
          <thumbs-up></thumbs-up>
          {{ computedData.likeCount }} 赞
        </span>
        <span class="meta-item">
          <star></star>
          {{ computedData.favoriteCount }} 收藏
        </span>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits, computed } from "vue";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import "dayjs/locale/zh-cn";
import { ThumbsUp, Star } from 'lucide-vue-next';

dayjs.extend(relativeTime);
dayjs.locale("zh-cn");

const props = defineProps({
  data: {
    type: Object,
    required: false,
    default: () => ({})
  }
});

const defaultData = {
  id: 1,
  userId: 1,
  title: "深入理解Spring Boot",
  description: "一本讲解Spring Boot原理的书籍",
  category: "book",
  url: "https://example.com/spring-boot",
  coverUrl: "https://dummyimage.com/120x80/ddd/000.png&text=SpringBoot",
  createdAt: "2025-03-22T09:15:50",
  userVO: {
    id: 1,
    username: "vks",
    email: "vkswhu@outlook.com",
    nickname: "Vcats",
    avatarUrl: "https://dummyimage.com/50x50/666/fff.png&text=V"
  },
  isLiked: false,
  isFavorite: false,
  likeCount: 118,
  commentCount: 6,
  favoriteCount: 97,
  viewCount: "1.4k"
};

const computedData = computed(() =>
  Object.keys(props.data).length ? props.data : defaultData
);

const emit = defineEmits(["openDetail"]);

const goToDetail = () => {
  emit("openDetail", computedData.value.id);
};

const formatTime = (time) => {
  return time ? dayjs(time).fromNow() : "未知时间";
};
</script>

<style scoped>
.resource-card {
  display: block;
  padding: 0px 16px;
  margin-bottom: 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;

  /* 水墨风：宣纸色背景 + 灰墨边框 + 墨晕阴影 */
  background-color: #fdfcf9;
  border: 5px solid #2f2f2f30;
  border-radius: 12px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.06),
              inset 0 0 8px rgba(0, 0, 0, 0.03);
  transition: all 0.3s ease-in-out;
}

.resource-card:hover {
  background-color: #f9f7f2;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.15),
              inset 0 0 8px rgba(0, 0, 0, 0.05);
  transform: scale(1.01);
}

.header {
  position: relative;
  height: 50px;
}

.user-info {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
  font-weight: bold;
  color: #222;
  font-family: "STKaiti", "FZKai-Z03S", serif;
  font-size: 15px;
}

.created-at {
  position: absolute;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  font-size: 12px;
  color: #888;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.text-content {
  margin-bottom: 1px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  font-family: "STKaiti", "FZKai-Z03S", serif;
  color: #1c1c1c;
  margin-top: 0px;
  margin-bottom: 10px;
  line-height: 1.4;
}

.description {
  font-size: 14px;
  color: #4a4a4a;
  margin: 0;
  line-height: 1.6;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta-info {
  display: flex;
  font-size: 14px;
  color: #444;
  gap: 80px;
  margin-top: 4px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 可选：鼠标 hover 波纹 */
.resource-card::before {
  content: '';
  position: absolute;
  top: var(--mouse-y, 50%);
  left: var(--mouse-x, 50%);
  width: 0;
  height: 0;
  background: radial-gradient(circle, rgba(0,0,0,0.05) 10%, transparent 60%);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none;
  transition: width 0.6s ease, height 0.6s ease;
}

.resource-card:hover::before {
  width: 150px;
  height: 150px;
}

</style>
