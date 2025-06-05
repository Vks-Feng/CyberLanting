<template>
  <div class="resource-meta">
    <span class="meta-item" @click="$emit('update:like')">
      <ThumbsUp :size="20" :stroke-width="2.5" :class="{ active: isLiked }" />
      {{ likeCount }} 赞
    </span>
    <span class="meta-item" @click="$emit('update:favorite')">
      <Star :size="20" :stroke-width="2.5" :class="{ active: isFavorite }" />
      {{ favoriteCount }} 收藏
    </span>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
import { ThumbsUp, Star } from 'lucide-vue-next';

defineProps({
  isLiked: Boolean,
  isFavorite: Boolean,
  likeCount: Number,
  favoriteCount: Number,
});
</script>

<style scoped>
.resource-meta {
  display: flex;
  gap: 20px;
  padding: 10px 0;
  border-top: 1px solid #eaeaea;
}

.meta-item {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
  color: #555;
  transition: color 0.3s, transform 0.2s;
}

.meta-item:hover {
  color: #222;
  transform: scale(1.1);
}

.active {
  color: var(--primary-color, #ff5722);
}
</style>
