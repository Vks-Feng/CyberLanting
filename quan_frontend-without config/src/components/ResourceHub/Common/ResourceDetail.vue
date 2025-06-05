<script setup>
import { ref, watch, computed } from 'vue';
import { ElNotification } from 'element-plus';
import ResourceHeader from './ResourceHeader.vue';
import ResourceMeta from './ResourceMeta.vue';
import ResourceComment from './ResourceComment.vue';
import resourceCenter from '@/api/resource-center';
import MarkdownView from '@/components/Common/MarkdownView.vue';
import LinkPreview from '@/components/ResourceHub/Common/LinkPreview.vue';

// ✅ 定义 `props`
const props = defineProps({
  modelValue: Boolean,
  resourceId: Number,
});

// ✅ 定义 `emit`
const emit = defineEmits(['update:modelValue', 'update:like', 'update:favorite']);

// ✅ 资源对象（避免 undefined 访问错误）
const resource = ref({
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

// ✅ 监听 `resourceId` 变化，获取资源详情
watch(
  () => props.resourceId,
  async (newId) => {
    if (!newId) return;
    try {
      resource.value = {}; // 重置数据
      const data = await resourceCenter.getResourceDetail(newId);
      resource.value = data;
    } catch (error) {
      ElNotification({ message: '加载资源失败', type: 'error' });
    }
  },
  { immediate: true }
);

// ✅ 关闭 Drawer
const closeDrawer = () => {
  emit('update:modelValue', false);
};

// // ✅ 访问外部资源
// const externalUrl = computed(() => resource.value.url);
// const goToExternalResource = () => {
//   if (externalUrl.value) {
//     window.open(externalUrl.value, '_blank');
//   } else {
//     ElNotification({ title: '提示', message: '该资源没有外部链接', type: 'warning' });
//   }
// };

// ✅ 处理点赞
const handleLike = async () => {
  try {
    if (!resource.value.isLiked) {
      await resourceCenter.likeResource(props.resourceId);
      resource.value.isLiked = true;
      resource.value.likeCount++;
    } else {
      await resourceCenter.unlikeResource(props.resourceId);
      resource.value.isLiked = false;
      resource.value.likeCount--;
    }
  } catch {
    ElNotification({message: '操作失败', type: 'error' });
  }
};

// ✅ 处理收藏
const handleFavorite = async () => {
  try {
    if (!resource.value.isFavorite) {
      await resourceCenter.favoriteResource(props.resourceId);
      resource.value.isFavorite = true;
      resource.value.favoriteCount++;
    } else {
      await resourceCenter.unfavoriteResource(props.resourceId);
      resource.value.isFavorite = false;
      resource.value.favoriteCount--;
    }
  } catch {
    ElNotification({ message: '操作失败', type: 'error' });
  }
};
</script>

<template>
  <el-drawer
    :model-value="modelValue"
    title="资源详情"
    size="70%"
    @close="closeDrawer"
  >
    <div class="resource-detail" v-if="resource.id">
      <ResourceHeader :user="resource.userVO" :created-at="resource.createdAt ?? ''" />

      <h2 class="title">{{ resource.title }}</h2>
      <!-- <el-button class="ink-button"   type="primary" @click="goToExternalResource">访问资源</el-button> -->
      <!-- <div
        v-if="externalUrl"
        class="link-preview-card"
        @click="goToExternalResource"
      >
        <img
          class="favicon"
          :src="`https://www.google.com/s2/favicons?sz=64&domain_url=${externalUrl}`"
          alt="favicon"
        />
        <div class="link-info">
          <p class="link-title">{{ resource.title || '资源链接' }}</p>
          <p class="link-url">{{ externalUrl }}</p>
        </div>
      </div> -->
      <LinkPreview
        v-if="resource.url"
        :url="resource.url"
      />
      <el-alert v-else title="该资源没有外部链接" type="info" />

      <p class="description">资源描述：{{ resource.description }}</p>

      <!-- ✅ Markdown 内容渲染 -->
      <div class="content">
        <MarkdownView :content="resource.content" />
      </div>

      <ResourceMeta
        :is-liked="resource.isLiked"
        :is-favorite="resource.isFavorite"
        :like-count="resource.likeCount"
        :favorite-count="resource.favoriteCount"
        @update:like="handleLike"
        @update:favorite="handleFavorite"
      />

      <ResourceComment :resource-id="resourceId" />
    </div>

    <div v-else class="loading">
      <el-skeleton :rows="5" animated />
    </div>
  </el-drawer>
</template>

<style scoped>
/* Overall container for the resource detail */
.resource-detail {
  padding: 16px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Title styling */
.resource-detail .title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
  margin: 12px 0;
}

/* Resource description */
.resource-detail .description {
  font-size: 1rem;
  line-height: 1.5;
  color: #666;
  margin: 16px 0;
}

/* Button for visiting external resource */
.el-button.primary {
  background-color: #409EFF;
  border-color: #409EFF;
  color: white;
  padding: 8px 16px;
  font-weight: bold;
  border-radius: 4px;
  transition: background-color 0.3s, transform 0.3s;
}

.el-button.primary:hover {
  background-color: #66b1ff;
  transform: scale(1.05);
}

/* Meta section */
.resource-meta {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.resource-meta .like-count,
.resource-meta .favorite-count {
  font-size: 1rem;
  color: #666;
}

.resource-meta .like-button,
.resource-meta .favorite-button {
  background: none;
  border: none;
  color: #409EFF;
  cursor: pointer;
  font-size: 1rem;
  transition: color 0.3s;
}

.resource-meta .like-button:hover,
.resource-meta .favorite-button:hover {
  color: #66b1ff;
}

.resource-meta .like-button:focus,
.resource-meta .favorite-button:focus {
  outline: none;
}

/* Skeleton loader for when resource data is not available */
.loading {
  padding: 16px;
  text-align: center;
}

/* Adjusting the size of the drawer and providing better alignment */
.el-drawer__body {
  padding: 20px;
}

/* Ensure clean display when content is still loading */
.el-skeleton {
  background-color: #f1f1f1;
  border-radius: 8px;
}



/* .link-preview-card {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.link-preview-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.link-preview-card .favicon {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.link-preview-card .link-info {
  overflow: hidden;
}

.link-preview-card .link-title {
  font-weight: bold;
  font-size: 1rem;
  color: #333;
  margin: 0;
  line-height: 1.2;
}

.link-preview-card .link-url {
  font-size: 0.85rem;
  color: #666;
  margin: 4px 0 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
} */

</style>
