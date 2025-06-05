<!-- KoanGenerator.vue  -->
<template>
    <div class="koan-container">
      <p class="zen-text">{{ currentKoan }}</p>
    </div>
  </template>
   
  <script>
  export default {
    data() {
      return {
        koans: [
          "代码本无bug 明镜亦非台",
          "异步三千界 同步一花开",
          "变量生灭法 闭包是颠倒",
          "渲染无所住 方生帧率障",
          "网络延迟处 刹那即永恒"
        ]
      }
    },
    computed: {
      currentKoan() {
        return this.koans[Math.floor(Math.random()  * this.koans.length)] 
      }
    }
  }
  </script>
   
  <style scoped>
  .koan-container {
    position: absolute;
    bottom: 2rem;
    right: 2rem;
    padding: 1rem;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 8px;
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.05);
  }
   
  .zen-text {
    margin: 0;
    font-family: 'Ma Shan Zheng', cursive;
    color: #5a5a5a;
    font-size: 1.1rem;
    line-height: 1.6;
  }
  </style>