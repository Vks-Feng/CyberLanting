<template>
  <div class="circle-container">
    <div class="button top" @click="handleClick('top')">开始使用</div>
    <div class="button left" @click="handleClick('left')">尚未构建</div>
    <div class="word">卷</div>
    <div class="button right" @click="handleClick('right')">尚未构建</div>
    <div class="button bottom" @click="handleClick('bottom')">关于我们</div>
  </div>
</template>

<script>
export default {
  methods: {
    handleClick(position) {
      const routeMap = {
        top: '/login',
        bottom: '/about',
        left: '/nothing',
        right: '/nothing'
      };
      this.$router.push(routeMap[position]);
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&display=swap');

.circle-container {
  position: relative;
  width: 60vh;
  height: 60vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
  border-radius: 50%;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.word {
  font-size: 45vh;
  font-weight: bold;
  font-family: 'Ma Shan Zheng', cursive;
  position: absolute;
  z-index: 2;
  color: #1a1a1a;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.25);
  opacity: 0.95;
}

.button {
  position: absolute;
  background-color: #262626;
  color: #f5f5f5;
  text-align: center;
  line-height: 40px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.4s ease;
  font-family: 'SimSun', serif;
  font-size: 5vh;
  border: 1px solid #262626;
}

.button:hover {
  transform: scale(1.05);
  background-color: #f5f5f5;
  color: #262626;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.top {
  top: 0;
  left: 0;
  width: 100%;
  height: 10%;
}

.left, .right {
  writing-mode: vertical-lr;
  line-height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.left {
  top: 0;
  left: 0;
  width: 10%;
  height: 100%;
}

.right {
  top: 0;
  right: 0;
  width: 10%;
  height: 100%;
}

.bottom {
  bottom: 0;
  left: 0;
  width: 100%;
  height: 10%;
}
</style>
