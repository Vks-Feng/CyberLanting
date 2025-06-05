import {axiosClient} from './axios-api'
import store from '@/data/vuex-data';
import { ElNotification } from 'element-plus'

const postRequirement= async (data) => {
  try {
    const response = await axiosClient.post('/obengine/generateFollowup',data);
    console.log('响应数据:', response);
    store.commit('setQuestion', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '初始化数据失败',
      type: 'error',
    })
    console.error('请求失败:', error);
    return null;
  }
};

const postAnswer = async(Data)=>{
  try {
    const response = await axiosClient.post('/obengine/generateDetailedPlan', Data);
    console.log('Post answer successfully ', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '创建目标失败',
      type: 'error',
    })
    console.error('Error creating obj:', error.response?.data || error.message);
    throw error;
  }
}

export default {
    postRequirement,
    postAnswer
}