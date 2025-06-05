<template>
  <div @mousemove="handleMouseMove" @mousedown="handleMouseDown" class="container">
    <div
      v-for="(ink, index) in inks"
      :key="ink.id"
      :style="ink.style"
      :class="ink.class"
      ></div> 
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'

// 墨水圈数据
const inks = ref<{ style: Record<string, string | number>; id: number ;class:string}[]>([])

let inkId = 0 // 用来区分每个墨水圈的唯一标识符

const handleMouseMove = (event: MouseEvent) => {
  createInkCircle(event.clientX, event.clientY)
}

const handleMouseDown = (event: MouseEvent) => {
  createInkCircleDown(event.clientX, event.clientY)
}

const createInkCircle = (x: number, y: number) => {
    const batch = 7;
    inkId++;
    if(inkId%batch!=0)return;
  // 墨水圈初始大小
  const initialSize = 7 + Math.random() * 5
  const duration = 2400 // 动画时长，单位：毫秒

  const newInk = {
    style: {
      left: `${x - initialSize / 2}px`,
      top: `${y - initialSize / 2}px`,
      width: `${initialSize}px`,
      height: `${initialSize}px`,
      borderRadius: '50%',
      position: 'absolute',
      backgroundColor: 'rgba(0, 0, 0, 0.4)', // 墨水圈颜色
    //   animation: `growInk 700ms ease-out`, // 使用新的growInk动画
    },
    id: inkId, // 增加唯一标识符
    class: 'ink'
  }

  // 增加新的墨水圈
  inks.value.push(newInk)

  // 墨水圈动画结束后移除
  setTimeout(() => {
    inks.value = inks.value.filter((ink) => ink.id !== newInk.id)
  }, duration)
}

const createInkCircleDown = (x: number, y: number) => {
  // 墨水圈初始大小
  const initialSize = 20 + Math.random() * 10
  const duration = 1500 // 动画时长，单位：毫秒

  const newInk = {
    style: {
      left: `${x - initialSize / 2}px`,
      top: `${y - initialSize / 2}px`,
      width: `${initialSize}px`,
      height: `${initialSize}px`,
      borderRadius: '50%',
      position: 'absolute',
      backgroundColor: 'rgba(0, 0, 0, 0.8)', // 墨水圈颜色
    },
    id: inkId++, // 增加唯一标识符
    class: 'inkDrop'
  }

  // 增加新的墨水圈
  inks.value.push(newInk)

  // 墨水圈动画结束后移除
  setTimeout(() => {
    inks.value = inks.value.filter((ink) => ink.id !== newInk.id)
  }, duration)
}

onMounted(() => {
  // 页面加载后清除所有墨水圈
  inks.value = []
})

</script>

<style scoped>
.container {
  position: relative;
  height: 100vh;
  /* cursor: none; 隐藏默认鼠标 */
}

.ink {
    position: absolute;
    pointer-events: none;
    z-index: 9999;
    opacity: 0.7;
    animation: growInk 2500ms;
}

.inkDrop{
    position: absolute;
    pointer-events: none;
    z-index: 9999;
    opacity: 0.7;
    animation: growInkDrop 3000ms;
}

/* 定义墨水圈的动画 */
@keyframes growInk {
    0% {
        transform: scale(1);
        opacity: 1;
    }

    100% {
        transform: scale(3);
        opacity: 0;
    }
}

@keyframes growInkDrop{
    0% {
        transform: scale(1);
        opacity: 1;
    }

    100% {
        transform: scale(6);
        opacity: 0;
    }
}
</style>
