<script>
import { ref, computed } from 'vue'
import { useAuth } from '@/api/useAuth'
import BackToLandingButton from '@/components/Landing/BackToLandingButton.vue'

export default {
  components: {
    BackToLandingButton
  },
  setup() {
    const isLogin = ref(true)
    const username = ref('')
    const email = ref('')
    const nickname = ref('')
    const password = ref('')
    const confirmPassword = ref('')
    const verificationCode = ref('')
    const isCodeSent = ref(false)
    const codeCountdown = ref(0)
    let countdownTimer = null

    const { loading, errorMessage, login, register, sendVerificationCode } = useAuth()

    // 判断邮箱格式是否正确
    const isEmailValid = computed(() => {
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        return emailPattern.test(email.value)
    })

    const toggleForm = () => {
      isLogin.value = !isLogin.value
      // 清空表单数据
      username.value = ''
      email.value = ''
      nickname.value = ''
      password.value = ''
      confirmPassword.value = ''
      verificationCode.value = ''
      isCodeSent.value = false
      codeCountdown.value = 0
      if (countdownTimer) clearInterval(countdownTimer)
    }

    const handleSubmit = async () => {
      try {
        if (isLogin.value) {
          await login({
            email: email.value,
            password: password.value,
          });
          // 登录成功后，可以重定向到主页或其他页面
          console.log('登录成功');
          router.push('/home');
        } else {
          await register({
            username: username.value,
            email: email.value,
            nickname: nickname.value,
            password: password.value,
            confirmPassword: confirmPassword.value,
            verificationCode: verificationCode.value,
          });
          // 注册成功后，可以重定向到登录页或主页
          router.push('/login');
        }
        // 可选：根据需要处理成功后的逻辑
      } catch (error) {
        // 根据响应拦截器的处理，error.message 包含业务异常信息
        if (error.message) {
          errorMessage.value = error.message;
        } else {
          // 处理其他类型的错误
          errorMessage.value = '提交失败，请稍后重试';
          console.error('提交错误:', error);
        }
      }
    };

    const handleSendCode = async () => {
      if (!isEmailValid.value) {
        errorMessage.value = '请输入正确的邮箱格式'
        return
      }
      try {
        await sendVerificationCode({ email: email.value })
        isCodeSent.value = true
        codeCountdown.value = 60
        errorMessage.value = ''

        countdownTimer = setInterval(() => {
          if (codeCountdown.value > 0) {
            codeCountdown.value--
          } else {
            clearInterval(countdownTimer)
            isCodeSent.value = false
          }
        }, 1000)
      } catch (error) {
        console.log(error)
        errorMessage.value = '验证码发送失败，请重试'
      }
    }

    return {
      isLogin,
      username,
      email,
      nickname,
      password,
      confirmPassword,
      verificationCode,
      isCodeSent,
      codeCountdown,
      isEmailValid,
      loading,
      errorMessage,
      toggleForm,
      handleSubmit,
      handleSendCode
    }
  }
}
</script>

<template>
  <div class="login-register-container">
    <BackToLandingButton />
    <div class="ink-card form-container">
      <h2 class="ink-title">{{ isLogin ? '登录' : '注册' }}</h2>
      <div class="error-message" v-if="errorMessage">{{ errorMessage }}</div>
      <form @submit.prevent="handleSubmit">
        <template v-if="!isLogin">
          <div class="input-group">
            <label for="username">用户名</label>
            <input class="ink-input" type="text" id="username" v-model="username" required />
          </div>
        </template>
        <div class="input-group">
          <label for="email">邮箱</label>
          <input class="ink-input" type="email" id="email" v-model="email" required />
        </div>
        <!-- 只有当邮箱格式正确时才显示验证码输入框 -->
        <template v-if="!isLogin && isEmailValid">
          <div class="input-group">
            <label for="verificationCode">验证码</label>
            <div class="verification-container">
              <input class="ink-input" type="text" id="verificationCode" v-model="verificationCode" required />
              <button 
                class="ink-button small-button"
                type="button"
                @click="handleSendCode"
                :disabled="isCodeSent"
              >
                {{ isCodeSent ? `重新获取(${codeCountdown}s)` : '获取验证码' }}
              </button>
            </div>
          </div>
        </template>

        <template v-if="!isLogin">
          <div class="input-group">
            <label for="nickname">昵称</label>
            <input class="ink-input" type="text" id="nickname" v-model="nickname" required />
          </div>
        </template>
        <div class="input-group">
          <label for="password">密码</label>
          <input class="ink-input" type="password" id="password" v-model="password" required />
        </div>
        <div class="input-group" v-if="!isLogin">
          <label for="confirmPassword">确认密码</label>
          <input class="ink-input" type="password" id="confirmPassword" v-model="confirmPassword" required />
        </div>
        <button 
          class="ink-button" 
          type="submit" 
          :disabled="loading"
        >
          {{ loading ? '处理中...' : (isLogin ? '行启' : '启名') }}
        </button>
      </form>
      <p class="toggle-form" @click="toggleForm">{{ isLogin ? '没有账号？注册' : '已有账号？登录' }}</p>
    </div>
  </div>
</template>

<style scoped>
.login-register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: var(--paper-white);
  background-image: 
    linear-gradient(rgba(0, 0, 0, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 0, 0, 0.02) 1px, transparent 1px);
  background-size: 20px 20px;
}

.form-container {
  background-color: var(--paper-white);
  padding: 40px 50px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  width: 400px;
  text-align: center;
  border: 1px solid var(--ink-gray);
}

.form-container h2 {
  color: var(--ink-black);
  margin-bottom: 30px;
  font-weight: normal;
  font-size: 28px;
  font-family: 'Ma Shan Zheng', cursive;
}

.input-group {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  text-align: left;
}

.input-group label {
  flex: 0 0 80px;
  color: var(--ink-gray);
  font-size: 16px;
}

.input-group input {
  flex: 1;
  padding: 12px 15px;
  font-size: 16px;
}

button {
  width: 100%;
  padding: 12px;
  font-size: 18px;
  margin-top: 10px;
}

.toggle-form {
  margin-top: 24px;
  color: var(--ink-gray);
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
  text-decoration: underline;
  text-underline-offset: 4px;
  padding: 4px 8px;
}

.toggle-form:hover {
  color: var(--ink-black);
  text-decoration-thickness: 2px;
}

.error-message {
  color: #8B0000;
  font-size: 16px;
  margin: 10px 0;
  text-align: center;
  font-family: 'SimSun', serif;
}

/* 添加水墨效果的输入框焦点样式 */
.ink-input:focus {
  border-color: var(--ink-black);
  box-shadow: 0 0 0 2px rgba(38, 38, 38, 0.1);
}

/* 优化按钮样式 */
.ink-button {
  background-color: var(--ink-black);
  color: var(--paper-white);
  border: 2px solid var(--ink-black);
  font-family: 'SimSun', serif;
  font-size: 18px;
  padding: 12px 24px;
  position: relative;
  overflow: hidden;
  transition: all 0.4s ease;
  letter-spacing: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.ink-button:hover {
  background-color: var(--paper-white);
  color: var(--ink-black);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.ink-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.ink-button::after {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  top: 50%;
  left: 50%;
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.6s ease, height 0.6s ease;
}

.ink-button:hover::after {
  width: 200%;
  height: 200%;
}

.ink-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.verification-container {
  display: flex;
  gap: 10px;
}

.small-button {
  padding: 5px 10px;
  font-size: 10px;  
}
</style>
