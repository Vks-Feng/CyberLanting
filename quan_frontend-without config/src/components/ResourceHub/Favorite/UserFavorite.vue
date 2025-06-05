<script setup>
import { ref, onMounted } from 'vue';
import ResourceArticle from '../Common/ResourceArticleCard.vue';
import resourceCenter from '@/api/resource-center';
import ResourceDetail from '../Common/ResourceDetail.vue';
import CategorySelector from '../Common/CategorySelector.vue';
import { ElMessage } from 'element-plus';
import { debounce } from 'lodash-es'; // 防抖处理滚动加载

const resources = ref([]); // 资源列表
const loading = ref(false); // 是否正在加载
const finished = ref(false); // 是否加载完成
const page = ref(1); // 当前页
const pageSize = 5; // 每页数量
const selectedCategory = ref(''); // 当前选择的分类

// 默认资源结构，防止字段缺失报错
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

// 切换分类时触发，清空并加载新分类数据
const changeCategory = (category) => {
  if (category === selectedCategory.value) return;
  selectedCategory.value = category;
  resources.value = [];
  page.value = 1;
  finished.value = false;
  loadResources(); // 加载新分类的第一页
};

// 主加载逻辑
const loadResources = async () => {
  if (loading.value || finished.value) return;

  const currentCategory = selectedCategory.value;
  loading.value = true;

  try {
    const response = await resourceCenter.getResourceFavorites(page.value, pageSize, currentCategory);
    console.log('API 返回的数据:', response);

    // 如果切换分类过程中请求返回结果，直接丢弃
    if (selectedCategory.value !== currentCategory) return;

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
  } finally {
    loading.value = false;
  }
};

// 防抖滚动加载处理
const throttledLoadResources = debounce(loadResources, 300);

// 详情抽屉控制
const drawerVisible = ref(false);
const selectedResourceId = ref(null);
const openDetailDrawer = (resourceId) => {
  selectedResourceId.value = resourceId;
  drawerVisible.value = true;
};

// 初始加载
onMounted(() => {
  loadResources();
});
</script>

<template>
  <el-scrollbar height="100vh">
    <div
      class="resource-list"
      v-infinite-scroll="throttledLoadResources"
      :infinite-scroll-disabled="loading || finished"
      infinite-scroll-distance="10"
    >
      <h3>用户收藏</h3>

      <!-- 分类选择器 -->
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

      <!-- 空状态或加载状态 -->
      <el-empty v-if="finished && resources.length === 0" description="暂无资源" class="empty_notice" />
      <el-loading v-if="loading" text="加载中..." />

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
