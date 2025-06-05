<template>
    <el-drawer v-model="drawerVisible" title="发表动态" size="50%">
      <el-input v-model="newPostContent" placeholder="说点什么..." type="textarea" rows="4" />
      <el-button type="primary" @click="publishPost">发布</el-button>
    </el-drawer>
  </template>
  
  <script setup>
  import { ref, defineEmits } from "vue";
  
  const newPostContent = ref("");
  const drawerVisible = ref(false);
  
  const emit = defineEmits(["postPublished"]);
  
  const publishPost = () => {
    if (!newPostContent.value.trim()) return;
  
    const newPost = {
      id: Date.now(),
      author: "你", // 可替换成当前用户
      content: newPostContent.value,
      likes: 0,
      comments: [],
    };
  
    emit("postPublished", newPost); // 发送事件给父组件
    newPostContent.value = "";
    drawerVisible.value = false;
  };
  
  defineExpose({
    openDrawer: () => {
      drawerVisible.value = true;
    },
  });
  </script>
  