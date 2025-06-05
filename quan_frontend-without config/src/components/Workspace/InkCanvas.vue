<!-- InkCanvas.vue  -->
<template>
    <div class="ink-canvas">
      <div 
        v-for="wave in waves" 
        :key="wave.id" 
        class="ink-wave"
        :style="wave.style" 
      ></div>
    </div>
  </template>
   
  <script>
  export default {
    data() {
      return {
        waves: [],
        waveCounter: 0 
      }
    },
    methods: {
      createWave(x, y) {
        this.waves.push({ 
          id: this.waveCounter++, 
          style: {
            left: x + 'px',
            top: y + 'px',
            width: '0px',
            height: '0px',
            opacity: 1 
          }
        })
        
        setTimeout(() => {
          this.waves.shift() 
        }, 2000)
      }
    }
  }
  </script>
   
  <style scoped>
  .ink-canvas {
    position: fixed;
    top: 0;
    left: 0;
    pointer-events: none;
  }
   
  .ink-wave {
    position: absolute;
    border: 2px solid rgba(127, 90, 240, 0.3);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    animation: ripple 1.5s ease-out;
  }
   
  @keyframes ripple {
    from {
      width: 0;
      height: 0;
      opacity: 1;
    }
    to {
      width: 300px;
      height: 300px;
      opacity: 0;
    }
  }
  </style>