<template>
    <div class="calligraphy-composition">
      <!-- 核心文字结构 -->
      <div class="seal-character" :style="characterStyle">卷</div>
   
      <!-- 笔画按钮系统 -->
      <div class="stroke-buttons">
        <button 
          v-for="(stroke, index) in strokes" 
          :key="index"
          class="stroke"
          :class="[
            `stroke-${index}`, 
            { 'stroke-active': activeStroke === index }
          ]"
          @mouseenter="activeStroke = index"
          @mouseleave="activeStroke = null"
          :style="getStrokeStyle(index)">
          <span class="stroke-label">{{ stroke.label  }}</span>
        </button>
      </div>
    </div>
  </template>
   
  <script>
  export default {
    name: 'CalligraphyComposition',
    data() {
      return {
        strokes: [
          { label: '—', type: 'horizontal', pos: 'top' },
          { label: '丨', type: 'vertical', pos: 'right' },
          { label: '—', type: 'horizontal', pos: 'bottom' },
          { label: '丨', type: 'vertical', pos: 'left' }
        ],
        activeStroke: null,
        componentSize: 480 
      }
    },
    computed: {
      characterStyle() {
        return {
          fontSize: `${this.componentSize  * 0.4}px`,
          textShadow: this.activeStroke  !== null 
            ? '2px 2px 4px rgba(0,0,0,0.15)' 
            : '1px 1px 3px rgba(0,0,0,0.1)'
        }
      }
    },
    methods: {
      getStrokeStyle(index) {
        const stroke = this.strokes[index] 
        const baseStyle = {
          width: stroke.type  === 'horizontal' ? '60%' : '12%',
          height: stroke.type  === 'vertical' ? '60%' : '12%',
          transform: this.getPositionTransform(stroke.pos) 
        }
        return stroke.type  === 'horizontal' 
          ? { ...baseStyle, transformOrigin: 'center' } 
          : baseStyle 
      },
      getPositionTransform(pos) {
        const offset = this.componentSize  * 0.3 
        const transforms = {
          top: `translateY(-${offset}px) rotate(0deg)`,
          right: `translateX(${offset}px) rotate(90deg)`,
          bottom: `translateY(${offset}px) rotate(180deg)`,
          left: `translateX(-${offset}px) rotate(270deg)`
        }
        return transforms[pos]
      }
    }
  }
  </script>
   
  <style lang="scss" scoped>
  @import url('https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@900&display=swap'); 
   
  .calligraphy-composition {
    position: relative;
    width: v-bind('componentSize + "px"');
    height: v-bind('componentSize + "px"');
    margin: 2rem auto;
   
    .seal-character {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-family: 'Noto Serif SC', serif;
      color: #2d3436;
      transition: text-shadow 0.3s ease;
      z-index: 10;
    }
   
    .stroke-buttons {
      .stroke {
        position: absolute;
        top: 50%;
        left: 50%;
        background: rgba(255,255,255,0.95);
        border: 2px solid #dcdde1;
        border-radius: 2px;
        cursor: pointer;
        transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        backface-visibility: hidden;
   
        &-label {
          position: absolute;
          font-family: 'Noto Serif SC', serif;
          color: #636e72;
          transition: inherit;
        }
   
        /* 横画样式 */
        &[class*="horizontal"] {
          .stroke-label {
            left: 50%;
            transform: translateX(-50%);
            top: -1.2em;
          }
          &:hover {
            border-color: #74b9ff;
            box-shadow: 0 3px 8px rgba(116, 185, 255, 0.2);
          }
        }
   
        /* 竖画样式 */
        &[class*="vertical"] {
          .stroke-label {
            left: 1.2em;
            top: 50%;
            transform: translateY(-50%);
          }
          &:hover {
            border-color: #ff7675;
            box-shadow: 0 3px 8px rgba(255, 118, 117, 0.2);
          }
        }
   
        /* 激活态微调 */
        &-active {
          z-index: 20;
          &.stroke-0, &.stroke-2 { 
            transform: translateY(calc(v-bind('componentSize * -0.3')px - 3px)) !important;
          }
          &.stroke-1, &.stroke-3 { 
            transform: translateX(calc(v-bind('componentSize * 0.3')px + 3px)) !important;
          }
        }
      }
    }
  }
  </style>