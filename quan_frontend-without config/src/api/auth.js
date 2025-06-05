import {axiosClient} from "@/api/axios-api"

export const auth = {
  // 登录
  login(data) {
    return axiosClient.post('/login', data)
  },

  // 注册
  register(data) {
    return axiosClient.post('/register', data)
  },

  // 登出
  logout() {
    return axiosClient.post('/user/logout')
  },

  // 发送验证码
  sendVerificationCode(email) {
    const encodedEmail = encodeURIComponent(email);
    return axiosClient.post(`/send-code?email=${encodedEmail}`);
  },

  // 获取用户信息
  getUserInfo() {
    return axiosClient.get('/user/user')
  }
} 