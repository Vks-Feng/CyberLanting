<template>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height:100vh;">
        <!-- 一级菜单 -->
        <el-aside width="64px" class="sidebar">
          <div class="main-menu">
            <el-menu :default-active="$route.path" :router="true">
              <!-- 头像 -->
              <div class="avatardiv">
                  <el-dropdown>
                    <el-avatar size="large" :src=getUserInfo().avatarUrl||defaultAvatar circle></el-avatar>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item  @click="gotoUserCenter">修己斋</el-dropdown-item>
                        <el-dropdown-item  @click="logout">息行</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
              </div>

              <!-- 第一项: 主页dashboard -->
              <el-menu-item index="/home/dashboard" style="margin-top: 20px;">
                <div class="icon-menu">
                 <img src="/images/icon/dashboard.svg"  style="width: 20px;"/>
                 <span style="margin-top: -15px" >总览堂</span>
                </div>
              </el-menu-item>

              <!-- 第二项: 任务列表objhub -->
              <el-menu-item index="/home/objMain">
                <div class="icon-menu">
                 <img src="/images/icon/task.svg"  style="width: 20px;"/>
                 <span style="margin-top: -15px" >星枢台</span>
                </div>
              </el-menu-item>
    
              <!-- 第三项：社交圈peerhub -->
              <el-menu-item index="/home/peerhub">
                <!-- <template #title> -->
                  <!-- <el-icon><UserFilled /></el-icon> -->
                <!-- </template> -->
                 <div class="icon-menu">
                 <img src="/images/icon/peerCircle.svg"  style="width: 20px;"/>
                 <span style="margin-top: -15px" >墨络轩</span>
                </div>
              </el-menu-item>
    
              <!-- 第四项：资源中心resourcehub -->
              <el-menu-item index="/home/resourcehub">
                <div class="icon-menu">
                 <img src="/images/icon/resource.svg"  style="width: 30px;"/>
                 <span style="margin-top: -15px" >衔筑庐</span>
                </div>
              </el-menu-item>
    
              <!-- 第五项：先放统计界面，后续会由主界面按钮跳转
              <el-menu-item index="/home/analysis">
                <template #title>
                  <el-icon><TrendCharts /></el-icon>
                </template>
              </el-menu-item> -->
            </el-menu>
          </div>
        </el-aside>

        <el-container class="main-container">
          <!-- 内容区域 -->
          <el-main style="overflow:hidden !important; height: 100%;">
            <router-view></router-view>
          </el-main>
        </el-container>
      </el-container>
    </div>
</template>
  
<script lang="ts" setup>
import { ref,onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { getUserInfo } from '@/api/userinfo-api';

import {
  DataBoard,
    House,
    List,
    Menu,
    TrendCharts,
    UserFilled,
} from '@element-plus/icons-vue'
  
const router = useRouter();
const route = useRoute();
const defaultAvatar = "https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg";


// 在组件挂载时，如果是根路径，自动跳转到默认页面
onMounted(() => {
  if (route.path === '/home') {
    router.push('/home/dashboard');
  }
});

const gotoUserCenter = () => {
  router.push('/user');
}

const logout = () => {
  router.push('/login');
}

const gotoNothing = () => {
  router.push('/nothing');
}
</script>

<style scoped>
.common-layout {
  width: 100%;
  height: 100vh;
}

.layout-container-demo {
  display: flex;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  background-color: var(--el-menu-bg-color);
  z-index: 1000;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.main-menu {
  height: 100%;
  border-right: none;
  background-color: #f9f7f2;
}

.main-container {
  margin-left: 40px;
  flex: 1;
  min-height: 100vh;
  background-color: var(--el-bg-color);
  background-image: url('/images/email.jpg') ;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  background-color: rgba(255, 255, 255, 0.8);
}

.el-main {
  padding: 20px;
}

.avatardiv {
  padding: 12px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item) {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 !important;
}

:deep(.el-menu-item .el-icon) {
  margin: 0;
}

:deep(.el-menu-item.is-active) {
  background-color: #69a7c943;
  color: #607B8B;
}

:deep(.el-menu-item:hover) {
  background-color: #69a7c943;
}


</style>