<script setup>
import { ref } from "vue";

const searchQuery = ref("");
</script>

<template>
    <!-- 搜索栏 -->
    <el-header class="header">
        <el-input v-model="searchQuery" placeholder="" class="search-box" />
        <el-button  type="primary" class="ink-button">搜索</el-button>
    </el-header>
</template>


<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}
.search-box {
  flex: 1;
  margin-right: 10px;
}
</style>