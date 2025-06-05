import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { auth } from '@/api/auth'
import store from '@/data/vuex-data'

export function useAuth() {
  const router = useRouter()
  const loading = ref(false)
  const errorMessage = ref('')
  const isCodeSent = ref(false)
  const codeCountdown = ref(0)
  let countdownTimer = null

  // 邮箱格式验证
  const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    return emailRegex.test(email)
  }

  // 登录方法
  const login = async (credentials) => {
    try {
      loading.value = true
      errorMessage.value = ''
      const response = await auth.login(credentials)
      // 将用户信息存储到本地存储中
      localStorage.setItem('userInfos', JSON.stringify(response.userVO))
      localStorage.setItem('token', response.token)
      router.push('/home')
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 注册方法
  const register = async (userData) => {
    if (!validateEmail(userData.email)) {
      errorMessage.value = '请输入有效的邮箱地址'
      return Promise.reject(new Error('Invalid email'))
    }
    
    if (userData.password !== userData.confirmPassword) {
      errorMessage.value = '两次输入的密码不一致'
      return Promise.reject(new Error('Passwords do not match'))
    }

    try {
      loading.value = true
      errorMessage.value = ''

      await auth.register({
        username: userData.username,
        email: userData.email,
        nickname: userData.nickname,
        password: userData.password,
        captcha: userData.verificationCode, // 需要用户输入的验证码
        avatarUrl: "666" // 暂时忽略此字段
      })
      
      // 注册成功后自动登录
      await login({
        email: userData.email,
        password: userData.password
      })
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 发送邮箱验证码
  const sendVerificationCode = async (email) => {
    if (!validateEmail(email.email)) {
      errorMessage.value = '请输入正确的邮箱格式'
      return Promise.reject(new Error('Invalid email'))
    }

    if (isCodeSent.value) {
      return Promise.reject(new Error('请稍后再试'))
    }

    try {
      loading.value = true
      errorMessage.value = ''
      
      await auth.sendVerificationCode(email.email)
      
      isCodeSent.value = true
      codeCountdown.value = 60

      countdownTimer = setInterval(() => {
        if (codeCountdown.value > 0) {
          codeCountdown.value--
        } else {
          clearInterval(countdownTimer)
          isCodeSent.value = false
        }
      }, 1000)
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    errorMessage,
    login,
    register,
    validateEmail,
    sendVerificationCode,
    isCodeSent,
    codeCountdown
  }
}
