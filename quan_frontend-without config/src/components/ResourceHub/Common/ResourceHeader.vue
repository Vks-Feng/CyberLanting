<!-- ResourceHeader.vue -->
<template>
    <div class="resource-header">
      <el-avatar :src="user.avatarUrl" size="small" />
      <span class="username">{{ user.nickname }}</span>
      <span class="created-at">{{ formatTime(createdAt) }}</span>
    </div>
</template>
<script setup>
import { defineProps } from 'vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');
const props = defineProps({ user: Object, createdAt: String });
const formatTime = (time) => (time ? dayjs(time).fromNow() : '未知时间');
</script>

<style scoped>
.resource-header {
display: flex;
align-items: center;
gap: 10px;
}
.username {
font-weight: bold;
}
.created-at {
color: #888;
font-size: 12px;
}
</style>