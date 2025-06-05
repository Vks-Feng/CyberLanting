<template>
              <el-card class="info-card">
            <h3>声誉录</h3>
            <p>点赞数: {{ likes }}</p>
            <p>评论数: {{ comments }}</p>
            <p>收藏数: {{ favorites }}</p>
          </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import analysisApi from '@/api/analysis-api';

const likes = ref(0);
const comments = ref(0);
const favorites = ref(0);

//获取card数据
onMounted(async () => {
  //获取社交成就信息
  const socialResponse = await analysisApi.getUserSocialAchievement();
  likes.value = socialResponse.receivedLikes;
  comments.value = socialResponse.receivedComments;
  favorites.value = socialResponse.receivedFavorites;
});

</script>