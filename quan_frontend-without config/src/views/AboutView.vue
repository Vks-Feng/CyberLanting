<template>
  <div class="about-container">
    <BackToLandingButton />

    <h1 class="title">关于 Quan</h1>

    <div class="description">
      <p>
        Quan 是一款融合了目标管理、任务打卡与社交互动的全能型成长工具。我们致力于为每一位用户打造一个有温度的成长社区，让你在坚持自我提升的路上，不再孤单。
      </p>

      <div class="feature-section">
        <h2>我们能帮你做什么？</h2>
        <ul class="feature-list">
          <li>
            <i class="fas fa-check-circle"></i>
            <span><strong>打卡记录</strong> - 养成好习惯，从每天的一小步开始</span>
          </li>
          <li>
            <i class="fas fa-tasks"></i>
            <span><strong>任务管理</strong> - 拆解目标，科学安排每日待办</span>
          </li>
          <li>
            <i class="fas fa-calendar-alt"></i>
            <span><strong>日程规划</strong> - 时间有序，生活不乱</span>
          </li>
          <li>
            <i class="fas fa-bullseye"></i>
            <span><strong>目标体系</strong> - 梳理人生方向，清晰成长路径</span>
          </li>
          <li>
            <i class="fas fa-users"></i>
            <span><strong>社交激励</strong> - 和朋友一起进步，互相打气</span>
          </li>
        </ul>
      </div>

      <div class="social-section">
        <h2>我们相信成长是可以分享的</h2>
        <p>在 Quan，你不再是一个人在战斗：</p>
        <ul>
          <li>添加志同道合的朋友，组成专属成长圈</li>
          <li>发布你的学习成果和经验感悟</li>
          <li>查看好友的目标和完成情况，互相鼓励</li>
          <li>为重要任务撰写总结，分享给你的朋友</li>
        </ul>
      </div>

      <p>成长从来都不容易，但在 Quan，你会收获陪伴、鼓励和成就感。让我们一起，把目标变成现实。</p>
      <footer class="footer">
        <p>© 2025 Quan · 让成长可视，让进步有声</p>
      </footer>
    </div>
  </div>
</template>


<script>
import BackToLandingButton from '@/components/Landing/BackToLandingButton.vue'

export default {
  name: 'AboutView',
  components: {
    BackToLandingButton
  }
}
</script>

<style scoped>
.about-container {
  max-height: 100vh; 
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  max-width: 800px;
  margin: 60px auto;
  padding: 2.5rem;
  background: linear-gradient(to bottom right, #fdfcf9, #f5f2eb);
  border-radius: 12px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  font-size: 16px;
  font-family: "SimSun", "宋体", serif;
  border: 1px solid #e0e0e0;
  overflow: auto; 
}

.title {
  color: #2e2e2e;
  text-align: center;
  margin-bottom: 2.5rem;
  font-size: 30px;
  font-weight: bold;
  letter-spacing: 2px;
  border-bottom: 2px solid #2e2e2e;
  padding-bottom: 1rem;
  font-family: "KaiTi", "华文行楷", cursive;
}

.description {
  flex: 1;
  line-height: 1.9;
  color: #333;
  font-size: 17px;
  text-align: justify;
}

.feature-section,
.social-section {
  margin: 2rem 0;
  padding: 1.8rem;
  background: rgba(255, 255, 255, 0.85);
  border: 1px dashed #aaa;
  border-radius: 8px;
  transition: transform 0.3s;
}

.feature-section:hover,
.social-section:hover {
  transform: scale(1.01);
}

.feature-list {
  list-style: none;
  padding: 0;
}

.feature-list li {
  margin: 1.2rem 0;
  display: flex;
  align-items: center;
  font-size: 16.5px;
}

.feature-list i {
  margin-right: 0.8rem;
  color: #444;
  font-size: 18px;
}

ul {
  padding-left: 1.5rem;
}

li {
  margin: 0.8rem 0;
  position: relative;
  padding-left: 1rem;
}

li::before {
  content: "✦";
  position: absolute;
  left: 0;
  color: #666;
  font-size: 12px;
}

h2 {
  color: #2e2e2e;
  margin-bottom: 1rem;
  font-family: "华文行楷", "KaiTi", cursive;
  font-size: 22px;
  position: relative;
  padding-left: 1rem;
}

h2::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 5px;
  height: 70%;
  background-color: #444;
  border-radius: 2px;
}

p {
  margin: 1rem 0;
}

.footer {
  margin-top: 3rem;
  padding-top: 1.5rem;
  border-top: 1px solid #ddd;
  text-align: center;
  font-size: 14px;
  color: #666;
  font-family: "SimSun", "宋体", serif;
  opacity: 0.8;
}

.about-container::-webkit-scrollbar {
  width: 8px;
}

.about-container::-webkit-scrollbar-track {
  background: transparent;
}

.about-container::-webkit-scrollbar-thumb {
  background-color: rgba(100, 100, 100, 0.15);
  border-radius: 4px;
}

.about-container::-webkit-scrollbar-thumb:hover {
  background-color: rgba(100, 100, 100, 0.25);
}

</style>