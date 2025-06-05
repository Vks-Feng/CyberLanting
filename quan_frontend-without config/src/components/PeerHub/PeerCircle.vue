<template>
  <div class="peer-circle">
    <!-- 头部 -->
    <div class="header">
      <h2>朋友圈</h2>
      <!-- 发帖按钮 -->
      <!-- <el-button @click="showSendPostDrawer" class="post-btn">发帖</el-button> -->
    </div>
    <el-divider />

    <!-- 帖子列表 -->
    <div
      class="post-list"
      v-infinite-scroll="loadMore"
      :infinite-scroll-disabled="disabled"
      infinite-scroll-distance="5"
    >
      <el-card
        v-for="post in posts"
        :key="post.id"
        class="post-item"
        @click="showPostDetail(post)"
      >
        <div class="post-header">
          <span class="username">{{ post.user.username }}</span >
          <span class="time">{{ formatTime(post.createdAt) }}</span>
          <span class="meta-info1">{{ post.likeCount }}赞</span>
          <span class="meta-info2">{{ post.commentCount }}评论</span>
        </div>
        <p>
          {{
            post.content.length > 40
              ? post.content.substring(0, 40) + "..."
              : post.content
          }}
          <el-button v-if="post.content.length > 40" type="text"
            >查看详情</el-button
          >
        </p>


      </el-card>

      <p v-if="loading">加载中...</p>
      <p v-if="isEnd">没有更多了</p>

      <!-- 帖子详情抽屉 -->
      <post-detail :post="selectedPost" v-model:visible="detailDrawerVisible" />
      
      <!-- 发帖抽屉 -->
      <send-post ref="sendPostRef" @postPublished="addNewPost" />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import PostDetail from "./PostDetail.vue";
import SendPost from "./SendPost.vue";
import peerApi from "@/api/peer-api";
import { ElNotification } from "element-plus";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import "dayjs/locale/zh-cn";

dayjs.extend(relativeTime);
dayjs.locale("zh-cn");

const detailDrawerVisible = ref(false);
const posts = ref([]);
const selectedPost = ref(null);
const loading = ref(false);
const page = ref(1);
const pageSize = 5;
const sendPostRef = ref(null);
const isEnd = ref(false);

const getPosts = async () => {
  loading.value = true;
  try {
    const response = await peerApi.getFriendCircle(page.value, pageSize);
    const newPosts = response.list;

    if (newPosts.length === 0) {
      isEnd.value = true;
      loading.value = false;
      return;
    }

    posts.value = [...posts.value, ...newPosts];
    loading.value = false;

    if (newPosts.length < pageSize) {
      isEnd.value = true;
    }
  } catch (error) {
    ElNotification.error("加载帖子失败");
  }
};

const loadMore = () => {
  if (!isEnd.value) {
    page.value += 1;
    getPosts();
  }
};

const disabled = computed(() => {
  return isEnd.value || loading.value;
});

const showPostDetail = (post) => {
  selectedPost.value = post;
  detailDrawerVisible.value = true;
};

const showSendPostDrawer = () => {
  sendPostRef.value.openDrawer();
};

const addNewPost = async (post) => {
  try {
    await peerApi.postMessage(post);
    detailDrawerVisible.value = false;
    sendPostRef.value.resetForm();
    ElNotification.success({
      message: "帖子已发布",
    });
    await peerApi.getFriendCircle(1, pageSize);
  } catch (error) {
    ElNotification.error({
      message: "请检查网络连接",
    });
  }
};

const formatTime = (time) => {
  return time ? dayjs(time).fromNow() : "未知时间";
};

onMounted(() => {
  getPosts();
});
</script>

<style scoped>
.peer-circle {
  margin: auto;
  overflow: auto;
}



.post-list {
  overflow: auto;
  height: 650px;
  scrollbar-width: thin;
  scrollbar-color: #999 transparent;


}

/* .post-item {
  margin-bottom: 15px;
  cursor: pointer;
  padding: 12px 16px;
  border-radius: 3px;
  background: rbga(0, 0, 0, 1) !important ;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s, box-shadow 0.3s;
} */

.post-item {
  display: block;
  padding: 0px 16px;
  margin-bottom: 10px;
  margin-right: 10px;
  margin-left: 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  height: auto;

  /* 水墨风：宣纸色背景 + 灰墨边框 + 墨晕阴影 */
  background-color: #fdfcf9;
  border: 5px solid #2f2f2f30;
  border-radius: 12px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.06),
              inset 0 0 8px rgba(0, 0, 0, 0.03);
  transition: all 0.3s ease-in-out;
}

.post-item:hover {
  background-color: #f9f7f2;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.15),
              inset 0 0 8px rgba(0, 0, 0, 0.05);
  transform: scale(1.01);
}

/* 可选：鼠标 hover 波纹 */
.post-item::before {
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

.post-item:hover::before {
  width: 150px;
  height: 150px;
}


.post-btn {
  margin-bottom: 0px;
  font-weight: bold;
  font-family: 'Ma Shan Zheng', serif;
  font-size: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0px;
}

.username {
  font-weight: bold;
  font-family: "STKaiti", "FZKai-Z03S", serif;
  font-size: 20px;
  color: #222;
}

.time {
  font-size: 12px;
  color: gray;
  margin-left: 10px
}

.meta-info1 {
  margin-top: 8px;
  margin-left: 50px;
  font-size: 10px;
  color: #999;
}
.meta-info2 {
  margin-top: 8px;
  margin-left: 10px;
  font-size: 10px;
  color: #999;
}

.meta-info span {
  margin-right: 15px;
}

.post-header h4 {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-top: 0px;
  margin-bottom: 4px;
  display: flex;
  justify-content: flex-start;
  align-items: center;

}

.post-item p {
  font-size: 15px;
  color: #666;
}

.post-item el-button {
  font-size: 12px;
  color: #409eff;
}

.el-divider {
  margin-top:0px;
  margin-bottom: 10px;
}
</style>
