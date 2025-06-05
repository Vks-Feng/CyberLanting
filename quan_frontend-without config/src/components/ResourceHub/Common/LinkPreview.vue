<script setup>
import { ref, onMounted, watch } from 'vue';
import { ElSkeleton } from 'element-plus';
import { ExternalLink } from 'lucide-vue-next';
import resourceApi from '@/api/resource-center';

const props = defineProps({
  url: {
    type: String,
    required: true,
  },
});

const previewData = ref(null);
const imageSrc = ref('');
const isLoading = ref(true);
const DEFAULT_IMG = 'https://www.dummyimage.com/120x100&text=preview';

const fetchPreview = async () => {
  if (!props.url) return;
  isLoading.value = true;
  try {
    const res = await fetch(`https://api.microlink.io/?url=${encodeURIComponent(props.url)}`);
    const { data } = await res.json();
    previewData.value = data;
    const imgUrl = data.image?.url || data.icon?.url;
    if (!imgUrl) {
      imageSrc.value = DEFAULT_IMG;
      return;
    }
    const imgRes = await resourceApi.getExternalImage(data.image?.url || data.icon?.url); 
    const { base64, contentType } = imgRes;
    imageSrc.value = `data:${contentType};base64,${base64}`;
  } catch {
    previewData.value = null;
    imageSrc.value = DEFAULT_IMG;
  } finally {
    isLoading.value = false;
  }
};

const onImageError = () => {
  imageSrc.value = DEFAULT_IMG;
};

const openInNewTab = () => {
  if (props.url) window.open(props.url, '_blank');
};

onMounted(fetchPreview);
watch(() => props.url, fetchPreview);
</script>

<template>
  <div v-if="isLoading" class="link-preview-loading">
    <el-skeleton :rows="3" animated />
  </div>

  <div
    v-else-if="previewData"
    class="link-preview-card"
    @click="openInNewTab"
  >
    <img
      class="preview-image"
      :src="imageSrc"
      @error="onImageError"
    />
    <div class="preview-content">
      <div class="preview-header">
        <h4 class="preview-title">{{ previewData.title }}</h4>
        <ExternalLink class="external-icon" />
      </div>
      <p class="preview-description">{{ previewData.description }}</p>
      <span class="preview-url">{{ previewData.url }}</span>
    </div>
  </div>
</template>

<style scoped>
.link-preview-loading {
  padding: 16px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(6px);
}

.link-preview-card {
  display: flex;
  align-items: flex-start;
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid #ddd;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.3s, transform 0.2s;
  margin: 16px 0;
  backdrop-filter: blur(6px);
}

.link-preview-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.preview-image {
  width: 120px;
  height:100%;
  object-fit: cover;
  flex-shrink: 0;
  background-color: #f5f5f5;
}

.preview-content {
  padding: 12px;
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-title {
  font-size: 1rem;
  font-weight: bold;
  color: #222;
  margin: 0;
  line-height: 1.4;
  flex: 1;
}

.preview-description {
  font-size: 0.875rem;
  color: #555;
  margin: 6px 0;
  line-height: 1.4;
  max-height: 3.5em;
  overflow: hidden;
  text-overflow: ellipsis;
}

.preview-url {
  font-size: 0.75rem;
  color: #888;
  word-break: break-all;
}

.external-icon {
  width: 16px;
  height: 16px;
  color: #888;
  flex-shrink: 0;
  margin-left: 8px;
}
</style>
