<!-- 已弃用 -->

<!-- 用户菜单：在Home界面Hover在头像上弹出菜单 -->
<!-- 安装了一个插件floating-vue 具体信息在这里啦https://floating-vue.starpad.dev/ -->
<template>
    <VDropdown :distance="70" :triggers="['hover']" :delay="{ show: 10, hide: 1000 }" :arrow-padding="60" theme="user-menu">
      <UserProfile :user="user" />
      
      <template #popper>
        <div class="menu-content">
          <div class="user-info">
            <img :src="user.avatar" class="menu-avatar" alt="用户头像" />
            <div class="user-details">
              <p class="user-name">{{ user.name }}</p>
              <p class="user-email">{{ user.email }}</p>
            </div>
          </div>
          <div class="menu-divider"></div>
          <div class="menu-items">
            <router-link to="/user" class="menu-item">
              <i class="fas fa-user"></i>
              个人主页
            </router-link>
            <div class="menu-item">
              <i class="fas fa-cog"></i>
              设置
            </div>
            <div class="menu-item">
              <i class="fas fa-sign-out-alt"></i>
              退出
            </div>
          </div>
        </div>
      </template>
    </VDropdown>
  </template>
  
<script>
  import { defineComponent } from 'vue'
  import UserProfile from '@/components/Workspace/UserProfile.vue'
  
  export default defineComponent({
    name: 'UserMenu',
    components: {
      UserProfile
    },
    props: {
      user: {
        type: Object,
        required: true
      }
    }
  })
  </script>
  
  <style scoped>
  .menu-content {
    min-width: 200px;
    padding: 8px 0;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    padding: 12px 16px;
  }
  
  .menu-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 12px;
  }
  
  .user-details {
    flex: 1;
  }
  
  .user-name {
    font-weight: bold;
    margin: 0;
    font-size: 14px;
  }
  
  .user-email {
    color: #666;
    margin: 4px 0 0;
    font-size: 12px;
  }
  
  .menu-divider {
    height: 1px;
    background-color: #eee;
    margin: 8px 0;
  }
  
  .menu-items {
    padding: 4px 0;
  }
  
  .menu-item {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    color: #333;
    text-decoration: none;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .menu-item:hover {
    background-color: #f5f5f5;
  }
  
  .menu-item i {
    margin-right: 8px;
    width: 16px;
    color: #666;
  }
  </style>