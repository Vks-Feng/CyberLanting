<template>
  <div class="container">

    <!-- Quan Logo -->
    <div class="fade-wrapper" @click="createRipple($event)">
      <router-link to="/about">
        <img src="/images/logo.png" alt="Quan-Logo" class="logo" />
      </router-link>
    </div>

    <!-- Start Button -->
    <div class="fade-wrapper delay" @click="createRipple($event)">
      <router-link to="/login">
        <img src="/images/start_button.png" alt="开始使用" class="start-image" />
      </router-link>
    </div>

  </div>
</template>

<script setup>
import router from '@/router';

function createRipple(event) {
  const target = event.currentTarget;
  const ripple = document.createElement("span");
  ripple.className = "ripple";

  const rect = target.getBoundingClientRect();
  const size = Math.max(rect.width, rect.height);
  ripple.style.width = ripple.style.height = `${size}px`;
  ripple.style.left = `${event.clientX - rect.left - size / 2}px`;
  ripple.style.top = `${event.clientY - rect.top - size / 2}px`;

  target.appendChild(ripple);

  setTimeout(() => ripple.remove(), 800);
}
</script>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("/images/landing-bg.png");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: top;
}

/*  用 fade-wrapper 来承载进场动画，不影响内部 hover */
.fade-wrapper {
  position: relative;
  animation: inkFadeIn 1.2s ease-out both;
  animation-delay: 0.1s;
}

.fade-wrapper.delay {
  animation-delay: 0.6s;
}

.logo {
  position: relative;
  bottom: 60px;
  right: 20%;
  width: 400px;
  height: auto;
  cursor: pointer;
  transition: transform 0.4s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.start-image {
  position: relative;
  right: 50%;
  width: 120px;
  height: auto;
  cursor: pointer;
  transition: transform 0.3s ease, filter 0.3s ease;
}

.start-image:hover {
  transform: scale(1.05);
  filter: brightness(1.1);
}

/* 水墨浮现动画 */
@keyframes inkFadeIn {
  0% {
    opacity: 0;
    transform: scale(0.95);
    filter: blur(4px);
  }
  100% {
    opacity: 1;
    transform: scale(1);
    filter: blur(0);
  }
}

/* 涟漪动画 */
.ripple {
  position: absolute;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.2);
  transform: scale(0);
  animation: ripple-animation 0.8s ease-out;
  pointer-events: none;
  z-index: 10;
}

@keyframes ripple-animation {
  to {
    transform: scale(3.5);
    opacity: 0;
  }
}
</style>
