<!-- SideNavigation.vue  修改版 -->
<template>
    <nav class="side-nav">
      <div 
        v-for="item in navItems" 
        :key="item.id" 
        class="nav-item"
        @mouseenter="handleHover(item.id)" 
      >
        <i :class="item.icon"></i> 
        <span>{{ item.name  }}</span>
      </div>
      <div class="ink-effect"></div>
    </nav>
  </template>
   
  <script>
    export default {
    props: {
        navItems: {
        type: Array,
        required: true 
        }
    },
    methods: {
        handleHover(id) {
        this.$emit('nav-hover', id)
        }
    }
    }
  </script>
   
  <style scoped>
  .side-nav {
    width: 240px;
    background: rgba(255,255,255,0.95);
    padding: 2rem 1rem;
    box-shadow: 
      5px 0 15px rgba(0, 0, 0, 0.05),
      inset -2px 0 0 rgba(58,27,27,0.1);
    border-right: 1px solid #3a1b1b33;
  }
   
  .nav-item {
    position: relative;
    padding: 1.5rem;
    color: #3a1b1b;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    font-family: 'Ma Shan Zheng', cursive;
    font-size: 1.2rem;
    display: flex;
    align-items: center;
    gap: 1rem;
  }
   
  .nav-item:hover {
    transform: translateX(15px);
    background: rgba(127,90,240,0.05);
  }
   
  .nav-item::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    width: 0;
    height: 100%;
    background: rgba(58,27,27,0.05);
    transform: translateX(-50%);
    transition: all 0.6s ease;
    z-index: -1;
  }
   
  .nav-item:hover::after {
    width: 110%;
    height: 120%;
    box-shadow: 0 0 25px rgba(58,27,27,0.15);
  }
   
  .ink-effect {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    background: radial-gradient(
      circle at var(--x) var(--y),
      rgba(58,27,27,0.1) 0%,
      transparent 15%
    );
    opacity: 0;
    transition: opacity 0.3s;
  }
   
  .nav-item:hover ~ .ink-effect {
    opacity: 1;
  }
  </style>