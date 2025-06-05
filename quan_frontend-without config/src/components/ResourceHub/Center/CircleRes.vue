<script setup>
import { ref, onMounted } from 'vue';
import ResourceArticle from '../Common/ResourceArticleCard.vue';
import resourceCenter from '@/api/resource-center';
import ResourceDetail from '../Common/ResourceDetail.vue';
import { ElMessage } from 'element-plus';
import CategorySelector from '../Common/CategorySelector.vue';
import { debounce } from 'lodash-es'; // 加入防抖

const resources = ref([]);
const loading = ref(false);
const finished = ref(false);
const page = ref(1);
const pageSize = 5;
const selectedCategory = ref('');

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

// 切换分类时调用，重置并加载资源
const changeCategory = (category) => {
  if (category === selectedCategory.value) return;
  selectedCategory.value = category;
  resources.value = [];
  page.value = 1;
  finished.value = false;
  loadResources();
};

// 主加载逻辑
const loadResources = async () => {
  if (loading.value || finished.value) return;

  const currentCategory = selectedCategory.value; // 捕获当前分类
  loading.value = true;

  try {
    const response = await resourceCenter.getResourcesList(page.value, pageSize, currentCategory);
    console.log('API 返回的数据:', response);

    // 分类被切换了就丢弃旧请求结果
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

// 添加防抖函数，防止滚动频繁触发
const throttledLoadResources = debounce(loadResources, 300);

// 打开详情抽屉
const drawerVisible = ref(false);
const selectedResourceId = ref(null);
const openDetailDrawer = (resourceId) => {
  console.log("打开详情，资源ID:", resourceId);
  selectedResourceId.value = resourceId;
  drawerVisible.value = true;
};

onMounted(() => {
  loadResources();
});
</script>


<template>
  <div class="resource-container">
    <div class="resource-header">
      <h3>圈内资源</h3>
      <CategorySelector @update:category="changeCategory" />
    </div>

    <el-scrollbar
      class="resource-scroll"
      v-infinite-scroll="throttledLoadResources"
      :infinite-scroll-disabled="loading || finished"
      infinite-scroll-distance="10"
    >
      <div class="resource-list">
        <ResourceArticle
          v-for="resource in resources"
          :key="resource.id"
          :data="resource"
          @openDetail="openDetailDrawer"
        />

        <ResourceDetail v-model="drawerVisible" :resource-id="selectedResourceId" />

        <el-empty v-if="finished && resources.length === 0" description="暂无资源" class="empty_notice" />
        <el-loading v-if="loading" text="加载中..." />

        <p v-if="finished && resources.length > 0" class="loading-text">没有更多资源了</p>
      </div>
    </el-scrollbar>
  </div>
</template>



<style scoped>
.resource-container {
  display: flex;
  flex-direction: column;
  height: 90vh; /* 或根据你的页面实际需求进行调整 */
  overflow: hidden;
}

.resource-header {
  padding: 10px;
  position: sticky;
  top: 0;
  z-index: 10;
}

.resource-scroll {
  flex: 1;
  overflow-y: auto;
}

.resource-list {
  padding: 10px;
}

.empty_notice {
  color: black;
}

.loading-text {
  text-align: center;
  color: #595959;
  margin: 10px 0;
}

</style>
