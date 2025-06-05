<!-- 登录页面的返回键 -->
<template>
  <router-link to="/" class="back-landing">
    <i class="fas fa-arrow-left"></i>
    <span>返回首页</span>
  </router-link>
</template>

<script>
export default {
  name: 'BackToLandingButton'
}
</script>

<style scoped>
.back-landing {
  position: absolute;
  top: 30px;
  left: 30px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  color: var(--ink-black);
  text-decoration: none;
  border: 2px solid var(--ink-black);
  border-radius: 4px;
  font-family: 'SimSun', serif;
  font-size: 16px;
  transition: all 0.3s ease;
  background-color: var(--paper-white);
}

.back-landing:hover {
  background-color: var(--ink-black);
  color: var(--paper-white);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.back-landing:active {
  transform: translateY(0);
}

.back-landing i {
  font-size: 14px;
}
</style> 