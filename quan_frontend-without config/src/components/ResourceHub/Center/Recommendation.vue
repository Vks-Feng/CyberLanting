<script setup>
import { ref, onMounted } from 'vue';
import ResourceArticle from '../Common/ResourceArticleCard.vue';
import resourceCenter from '@/api/resource-center';
import ResourceDetail from '../Common/ResourceDetail.vue';
import { ElMessage } from 'element-plus';

const resources = ref([]); // 资源列表
const loading = ref(false);
const finished = ref(false);
const page = ref(1);
const pageSize = 1;

// 预定义资源数据结构，确保每个资源对象都有默认值
const defaultResource = () => ({
  id: null,
  userId: null,
  title: '',
  description: '',
  category: '',
  url: '',
  content: '',
  createdAt: '',
  userVO: {
    id: null,
    username: '',
    email: '',
    nickname: '',
    avatarUrl: '',
  },
  isLiked: false,
  isFavorite: false,
  likeCount: 0,
  commentCount: 0,
  favoriteCount: 0,
});

// 获取资源列表
const loadResources = async () => {
  if (loading.value || finished.value) return;
  loading.value = true;

  try {
    const response = await resourceCenter.getResourcesList(page.value, pageSize);
    console.log('API 返回的数据:', response);

    // 确保 response 是数组
    const Resources = response.list;

    if (Resources.length) {
      resources.value.push(
        ...Resources.map((res) => ({
          ...defaultResource(), // 先填充默认值，防止字段缺失
          ...res, // 用 API 返回的数据覆盖默认值
        }))
      );
      page.value++;
    } else {
      finished.value = true; // 没有更多数据了
    }
  } catch (error) {
    ElMessage.error('加载资源失败');
    console.error('加载资源失败:', error);
  } finally {
    loading.value = false;
  }
};



const drawerVisible = ref(false);
const selectedResourceId = ref(null);

// 传递给子组件 id，用于请求详细数据
const openDetailDrawer = (resourceId) => {
  selectedResourceId.value = resourceId;
  drawerVisible.value = true;
};

// 组件挂载时加载数据
onMounted(() => {
  loadResources();
});
</script>

<template>
  <div class="resource-list" v-infinite-scroll="loadResources" infinite-scroll-disabled="loading || finished" infinite-scroll-distance="10">
    <h3>为你推荐</h3>
    <ResourceArticle
      v-for="resource in resources"
      :key="resource.id"
      :data="resource"
      @openDetail="openDetailDrawer"
    />
    <p v-if="loading" class="loading-text">加载中...</p>
    <p v-if="finished" class="loading-text">没有更多资源了</p>
      <!-- 详情弹出框 -->
      <ResourceDetail 
        v-model="drawerVisible" 
        :resource-id="selectedResourceId" 
      />

  </div>
</template>

<style scoped>
.resource-list {
  padding: 10px;
  max-height: 80vh;
  overflow-y: auto;
}
.loading-text {
  text-align: center;
  color: gray;
  margin: 10px 0;
}
</style>
