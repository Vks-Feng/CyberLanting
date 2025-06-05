<script setup>
import { ref, onMounted } from 'vue';
import ResourceArticle from '../ResourceHub/Common/ResourceArticleCard.vue';
import resourceCenter from '@/api/resource-center';
import ResourceDetail from '../ResourceHub/Common/ResourceDetail.vue';
import { ElMessage } from 'element-plus';
import CategorySelector from '../ResourceHub/Common/CategorySelector.vue';
import { getUserInfo } from '@/api/userinfo-api';
import { debounce } from 'lodash-es'; // 防抖防止无限滚动连发

// 状态管理
const resources = ref([]);
const loading = ref(false);
const finished = ref(false);
const page = ref(1);
const pageSize = 5;
const selectedCategory = ref('');

// 默认资源结构
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

// 切换分类逻辑
const changeCategory = (category) => {
  if (category === selectedCategory.value) return;
  selectedCategory.value = category;
  resources.value = [];
  page.value = 1;
  finished.value = false;
  loadResources();
};

// 加载资源数据
const loadResources = async () => {
  if (loading.value || finished.value) return;
  loading.value = true;

  try {
    const userId = getUserInfo()?.id;
    if (!userId) throw new Error('未获取到用户信息');

    const response = await resourceCenter.getUserResourcesList(
      userId,
      page.value,
      pageSize,
      selectedCategory.value
    );
    console.log('API 返回的数据:', response);

    if (response?.list?.length) {
      resources.value.push(
        ...response.list.map((res) => ({
          ...defaultResource(),
          ...res,
        }))
      );
      page.value++;
    } else {
      finished.value = true;
    }
  } catch (error) {
    ElMessage.error('加载资源失败');
    console.error('加载资源失败:', error);
    finished.value = true;
  } finally {
    loading.value = false;
  }
};

// 抽屉详情
const drawerVisible = ref(false);
const selectedResourceId = ref(null);
const openDetailDrawer = (resourceId) => {
  selectedResourceId.value = resourceId;
  drawerVisible.value = true;
};

// 使用防抖处理滚动加载
const throttledLoadResources = debounce(loadResources, 300);

// 初次加载
onMounted(() => {
  loadResources();
});
</script>

<template>
  <el-scrollbar height="80vh">
    <div
      class="resource-list"
      v-infinite-scroll="throttledLoadResources"
      :infinite-scroll-disabled="loading || finished"
      infinite-scroll-distance="10"
    >
      <!-- 分类选择 -->
      <CategorySelector @update:category="changeCategory" />

      <!-- 资源卡片 -->
      <ResourceArticle
        v-for="resource in resources"
        :key="resource.id"
        :data="resource"
        @openDetail="openDetailDrawer"
      />

      <!-- 抽屉详情 -->
      <ResourceDetail v-model="drawerVisible" :resource-id="selectedResourceId" />

      <!-- 空状态与提示 -->
      <el-empty v-if="finished && resources.length === 0" description="暂无资源" />
      <p v-if="finished && resources.length > 0" class="loading-text">没有更多资源了</p>
    </div>
  </el-scrollbar>
</template>


<style scoped>
.resource-list {
  padding: 10px;
}
.loading-text {
  text-align: center;
  color: gray;
  margin: 10px 0;
}
</style>
