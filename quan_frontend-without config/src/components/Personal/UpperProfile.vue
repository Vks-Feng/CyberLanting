<!-- 个人主页的上部分组件，主要展示用户基本信息 -->
<!-- <template>
  <div class="profile-container">
    <div class="profile-header">
      <div class="avatar-section">
        <img :src="user.avatar" alt="用户头像" class="avatar" />
      </div>
      <div class="user-info">
        <h2 class="username">{{ user.name }}</h2>
        <div class="user-details">
          <p class="user-id">ID: {{ user.id }}</p>
          <p class="user-email">邮箱: {{ user.email }}</p>
        </div>
      </div>
    </div>
  </div>
</template> -->

//<script>
//import { defineComponent } from 'vue'

// //ai生成，关于user的导入
// export default defineComponent({
//   name: 'UpperProfile',
//   props: {
//     user: {
//       type: Object,
//       required: true,
//       validator: (obj) => {
//         return obj && obj.avatar && obj.name && obj.email
//       }
//     }
//   }
// })
// </script>

{/* <style scoped>
.profile-container {
  padding: 2em;
  margin-top: 0;
  margin-right: auto;
  margin-left: auto;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(240, 240, 240, 0.8));
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  width: 90%;
  height: 18vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: visible;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  width: 100%;
}

.avatar-section {
  display: flex;
  justify-content: center;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border-color: #fff;
  border-width: 2px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  object-fit: cover;
  transition: transform 0.3s;
  margin-top: -50px;
}

.avatar:hover {
  transform: scale(1.1);
}

.user-info {
  text-align: center;
}

.username {
  margin: 0 0 6px 0;
  font-size: 1.5rem;
  font-weight: bold;
}

.user-details {
  color: #666;
  font-size: 0.9rem;
}

.user-details p {
  margin: 4px 0;
}
</style> */}
