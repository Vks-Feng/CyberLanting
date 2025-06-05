<!-- UserProfile.vue  -->
<template>
    <div class="user-panel">
      <img :src="user.avatar"  class="avatar" />
      <h3 class="username">{{ user.name  }}</h3>
    </div>
  </template>
   
  <script>
  export default {
    props: {
      user: {
        type: Object,
        required: true
      }
    }
  }
  </script>
   
  <style scoped>
  .user-panel {
    position: absolute;
    top: 2rem;
    right: 2rem;
    display: flex;
    align-items: center;
    gap: 1rem;
  }
   
  .avatar {
    width: 56px;
    height: 56px;
    border-radius: 50%;
    border: 2px solid #7f5af0;
    box-shadow: 0 0 10px rgba(127, 90, 240, 0.3);
  }
   
  .username {
    margin: 0;
    font-family: 'Calligraffitti', cursive;
    color: #3a3a3a;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
  }
  </style>