<script setup>
import { ref, defineEmits } from 'vue';

const categories = [
  { label: '全部', value: '' },
  { label: '文章', value: 'article' },
  { label: '书籍', value: 'book' },
  { label: '课程', value: 'course' },
  { label: '工具', value: 'tool' },
  { label: '心得', value: 'insight' }
];

const selectedCategory = ref('');
const emit = defineEmits(['update:category']);

const selectCategory = (value) => {
  selectedCategory.value = value;
  emit('update:category', value);
};
</script>

<template>
  <div class="category-nav">
    <div 
      v-for="item in categories" 
      :key="item.value" 
      class="category-item" 
      :class="{ active: selectedCategory === item.value }"
      @click="selectCategory(item.value)">
      {{ item.label }}
    </div>
  </div>
</template>

<style scoped>
.category-nav {
  display: flex;
  gap: 16px;
  padding: 10px;
  border-bottom: 2px solid #ebeef5;
}

.category-item {
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
  font-weight: 500;
}

.category-item:hover {
  background-color: #f5f7fa;
}

.category-item.active {
  color: 	#607B8B;
  border-bottom: 2px solid #607B8B;
}
</style>