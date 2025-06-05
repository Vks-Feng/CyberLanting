import {axiosClient} from './axios-api'
import { ElNotification } from 'element-plus'

const uploadFile = async (file) => {
    try {
      console.log(file)
      const formData = new FormData();
      formData.append("file", file);  // 将文件加入 FormData 中
      const response = await axiosClient.post('/files/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',  // 必须设置为 multipart/form-data
        },
      });
  
      ElNotification({
        message: '文件上传成功',
        type: 'success',
      });
  
      return response;  // 返回服务器响应的数据
  
    } catch (error) {
      ElNotification({
        message: '文件上传失败，请重试。',
        type: 'error',
      });
      console.error('Error uploading file:', error.response?.data || error.message);
      throw error;  // 抛出错误，供调用者处理
    }
  };
  

export default {
    uploadFile
}