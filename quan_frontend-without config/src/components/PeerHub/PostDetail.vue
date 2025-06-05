<!-- 帖子Drawer -->
<!-- Problem：评论以后刷新不及时，需要手动刷新 -->
<template>
  <el-drawer v-model="drawerVisible" title="帖子详情" size="50%" @update:model-value="updateVisible">
    <div class="layout">
      <!-- <el-avatar>{{ post?.user.avatarUrl }}</el-avatar> -->
      <h3>{{ post?.user.username }}</h3>
      <p>{{ post?.content }}</p>

      <el-row justify="start">
        <el-button link @click="likePost(post)" class="ink-style">
          <span>
            <ThumbsUp></ThumbsUp>
            {{ post.likeCount }}赞</span>
        </el-button>
        <el-button link class="ink-style">
          <span>
            <ChatDotSquare></ChatDotSquare>
            {{ comments.length }}评论</span>
        </el-button>
      </el-row>

      <el-divider style="margin-bottom: 3px" />


      <el-divider />
      <h4>评论</h4>
      <div style="display: flex">
        <el-input class="comment-input" v-model="newComment" placeholder="写下你的评论..." clearable />
        <el-button class="ink-button" type="success" @click="addComment(post.id)">发送</el-button>
      </div>


      <ul class="comment-list">
  <li v-for="comment in comments" :key="comment.id" class="comment-item">
    <img :src="comment.user.avatarUrl" alt="头像" class="avatar" />
    <div class="comment-content">
      <div class="comment-header">
        <span class="nickname">{{ comment.user.nickname }}</span>
        <span class="timestamp">{{ formatDate(comment.createdAt) }}</span>
      </div>
      <p class="comment-text">{{ comment.content }}</p>
    </div>
  </li>
</ul>

<div v-if="loadingComments" class="comment-wrapper">
  <span class="comment-tip">加载评论中...</span>
</div>
<div v-if="!loadingComments && comments.length === 0 && noMoreComments" class="comment-wrapper">
  <span class="comment-tip">暂无评论</span>
</div>
<div v-if="noMoreComments && comments.length > 0" class="comment-wrapper">
  <span class="comment-tip">没有更多评论了</span>
</div>


    </div>
  </el-drawer>
</template>

<script setup>
import { defineProps, ref, watch } from "vue";
import peerApi from "@/api/peer-api";
import { ElNotification } from "element-plus";

import dayjs from "dayjs";
import { ThumbsUp } from "lucide-vue-next";

const props = defineProps({
  post: Object,
  visible: Boolean,
});

const emit = defineEmits(["update:visible"]);
const drawerVisible = ref(props.visible);
const newComment = ref("");
const comments = ref([]);
const loadingComments = ref(false);
const noMoreComments = ref(false);

const updateVisible = (val) => {
  emit("update:visible", val);
};

const likePost = async (post) => {
  console.log("点赞的PostId：", post.id);

  try {
    const postId = post.id;
    if (post.isLiked) {
      await peerApi.deleteThumbsUp(postId);
      post.likeCount -= 1;
      console.log("取消点赞成功");
      post.isLiked = false;
    } else {
      await peerApi.postThumbsUp(postId);
      post.likeCount += 1;
      console.log("点赞成功");
      post.isLiked = true;
    }
  } catch (error) {
    ElNotification.error("操作失败，请重试");
  }
};

const fetchComments = async (postId) => {
  loadingComments.value = true;
  comments.value = []; // 先清空评论，防止显示上一个帖子的数据

  try {
    const response = await peerApi.getComments(postId);
    comments.value = response;
    console.log("正在获取的PostId：", postId);
    console.log("获取到的评论：", response);
    if (response.length === 0) {
      console.log("评论为空");
      loadingComments.value = false;
    } else {
      noMoreComments.value = true;
    }
  } catch (error) {
    ElNotification.error("获取评论失败，请重试");
  } finally {
    loadingComments.value = false;
  }
};

//发送评论以后，需要重新fetchComments
const addComment = async (postId) => {
  if (!newComment.value.trim()) {
    ElNotification.warning("评论内容不能为空");
    return;
  }

  try {
    await peerApi.postComment(postId, newComment.value);
    console.log("评论成功,帖子ID:", postId);
    newComment.value = "";

    //实时更新评论列表
    //这里重新用响应式逻辑编写会更好吗?
    await fetchComments(postId);
  } catch (error) {
    ElNotification.error("评论失败，请重试");
    console.log("评论失败原因：", error);
  }
};

watch(
  () => props.visible,
  async (newVal) => {
    drawerVisible.value = newVal;
    if (newVal && props.post) {
      await fetchComments(props.post.id);
    }
  }
);

// watch(
//   () => props.post?.id,async (newVal) => {
//     if(newVal){
//       await fetchComments(newVal);
//     }
//   });

watch(
  () => props.post,
  async (newPost) => {
    if (newPost?.id) {
      await fetchComments(newPost.id);
    }
  },
  { immediate: true }
);

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm');
};
</script>

<style scoped>
.comment-input .el-input__inner {
  background-color: transparent;
  border: 1px solid transparent;
  transition: all 0.2s ease-in-out;
  box-shadow: none;
}

.comment-input .el-input__inner:hover {
  border-color: rgba(100, 100, 100, 0.3);
  background-color: rgba(255, 255, 255, 0.05);
}

.comment-input .el-input__inner:focus {
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.ink-style {
  font-family: "Ma Shan Zheng", serif;
  font-size: 15px;
  color: gray;
}

.comment-list {
  list-style: none;
  padding: 0;
}

.comment-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 10px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #666;
}

.nickname {
  font-weight: bold;
  color: #333;
}

.timestamp {
  font-size: 12px;
  color: #999;
}

.comment-text {
  margin-top: 4px;
  font-size: 14px;
  color: #444;
}

.comment-wrapper {
  text-align: center;
  margin-top: 16px;
}

.comment-tip {
  /* display: inline-block; */
  font-family: "KaiTi", "楷体", "STKaiti", "Georgia", serif;
  font-size: 15px;
  color: #5b4b3a;
  /* background-color: rgba(250, 250, 245, 0.7); */
  padding: 8px 16px;
  /* border-radius: 12px;
  border: 1px solid #e5e1da; */
  /* box-shadow: 2px 2px 6px rgba(200, 190, 170, 0.2); */
  font-style: italic;
  letter-spacing: 1px;
}



</style>
