<template>
  <div class="common-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside
        width="25%"
        class="ob-bar"
        style="display: flex; flex-direction: column; height: 75vh"
      >
        <h2>衔筑庐</h2>
        <el-divider />

        <div class="sidebar">
          <el-button
            class="sidebar-btn ink-button"
            plain
            @click="activePage = 'ResourceCenter'"
          >
            资源中心
          </el-button>
          <el-button
            class="sidebar-btn ink-button"
            plain
            @click="activePage = 'UserFavorite'"
          >
            我的收藏
          </el-button>
          <el-button
            class="sidebar-btn ink-button"
            plain
            @click="activePage = 'CreatorMainPage'"
          >
            发布资源
          </el-button>
        </div>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <el-main>
          <component :is="currentComponent" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from "vue";
import ResourceCenter from "./Center/ResourceCenter.vue";
import CreatorMainPage from "./Creator/CreatorMainPage.vue";
import UserFavorite from "./Favorite/UserFavorite.vue";

const activePage = ref("ResourceCenter");

const currentComponent = computed(() => {
  switch (activePage.value) {
    case "ResourceCenter":
      return ResourceCenter;
    case "UserFavorite":
      return UserFavorite;
    case "CreatorMainPage":
      return CreatorMainPage;
    default:
      return ResourceCenter;
  }
});
</script>

<style scoped>
.el-container {
  height: 100vh;
}

.el-main {
  padding: 20px !important;
}

.footer {
  display: flex;
  justify-content: flex-end;
}

.sidebar {
  display: flex; 
  flex-direction: column;
}

.sidebar-btn {
  display: block;
  width: 90%;
  margin: 10px 10px 10px 12px;
  color: #2C2C2C;
  background-color: rgb(255, 255, 255, 0.5);
}
</style>
