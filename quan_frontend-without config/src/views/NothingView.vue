<template>
  <div class="nothing-container">
    <div class="home-button" @click="$router.push('/')">
      <i class="fas fa-home"></i>
      <span>回到首页</span>
    </div>
    <div class="glitch-text" data-text="404">404</div>
    <h1 class="main-text">都说了我没想到要干啥</h1>
    
    <div class="random-stuff">
      <p class="floating">🤪 这里什么都没有 🤪</p>
      <p class="spinning">💫 真的什么都没有 💫</p>
      <p class="bouncing">🎈 你还在看什么 🎈</p>
      <p class="shaking">🌈 快去别的地方吧 🌈</p>
      
      <div class="easter-egg" @click="triggerEasterEgg">
        <span>不要点我</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    triggerEasterEgg() {
      alert('我警告过你不要点的！现在你中毒了！\n.\n.\n.\n开玩笑的 😝');
      document.body.style.transform = 'rotate(180deg)';
      setTimeout(() => {
        document.body.style.transform = 'none';
      }, 2000);
    }
  }
}
</script>

<style scoped>
.nothing-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  color: white;
  overflow: hidden;
}

.home-button {
  position: fixed;
  top: 20px;
  left: 20px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  color: white;
}

.home-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.05);
}

.home-button i {
  font-size: 18px;
}

.glitch-text {
  font-size: 8rem;
  font-weight: bold;
  position: relative;
  text-shadow: 0.05em 0 0 #ff00ff, -0.05em -0.025em 0 #00ff00;
  animation: glitch 725ms infinite;
}

.main-text {
  font-size: 2rem;
  margin: 2rem 0;
  animation: rainbow 3s infinite;
}

.random-stuff {
  text-align: center;
}

.floating {
  animation: float 3s ease-in-out infinite;
}

.spinning {
  display: inline-block;
  animation: spin 4s linear infinite;
}

.bouncing {
  animation: bounce 1s ease infinite;
}

.shaking {
  animation: shake 0.5s ease infinite;
}

.easter-egg {
  margin-top: 2rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.easter-egg:hover {
  transform: scale(1.1);
  background: rgba(255, 255, 255, 0.3);
}

@keyframes glitch {
  0% { text-shadow: 0.05em 0 0 #ff00ff, -0.05em -0.025em 0 #00ff00; }
  14% { text-shadow: 0.05em 0 0 #ff00ff, -0.05em -0.025em 0 #00ff00; }
  15% { text-shadow: -0.05em -0.025em 0 #ff00ff, 0.025em 0.025em 0 #00ff00; }
  49% { text-shadow: -0.05em -0.025em 0 #ff00ff, 0.025em 0.025em 0 #00ff00; }
  50% { text-shadow: 0.025em 0.025em 0 #ff00ff, 0.05em 0 0 #00ff00; }
  99% { text-shadow: 0.025em 0.025em 0 #ff00ff, 0.05em 0 0 #00ff00; }
  100% { text-shadow: -0.025em 0 0 #ff00ff, -0.025em -0.025em 0 #00ff00; }
}

@keyframes rainbow {
  0% { color: #ff0000; }
  20% { color: #ffff00; }
  40% { color: #00ff00; }
  60% { color: #00ffff; }
  80% { color: #0000ff; }
  100% { color: #ff0000; }
}

@keyframes float {
  0% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
  100% { transform: translateY(0); }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}
</style>
