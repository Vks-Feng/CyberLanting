import { axiosClient } from './axios-api';
import { ElNotification } from 'element-plus';
// 创建资源
const createResource = async (data) => {
  try {
    const response = await axiosClient.post('/resources', data);
    console.log('资源创建成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '资源创建失败',
      type: 'error',
    });
    console.error('资源创建失败:', error.response?.data || error.message);
    return null;
  }
};

// 获取资源详情
const getResourceDetail = async (id) => {
  try {
    const response = await axiosClient.get(`/resources/${id}`);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法获取资源详情',
      type: 'error',
    });
    console.error('获取资源失败:', error.response?.data || error.message);
    return null;
  }
};

// 获取资源列表
const getResourcesList = async ( page, pageSize, category ) => {
  try {
    const response = await axiosClient.get('/resources', {
      params: {
        page,
        pageSize, 
        category
      }
    });
    console.log('获取资源列表:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法加载资源列表',
      type: 'error',
    });
    console.error('获取资源列表失败:', error.response?.data || error.message);
    return null;
  }
};

// 获取资源列表
const getUserResourcesList = async ( userId, page, pageSize, category ) => {
  try {
    const response = await axiosClient.get(`/resources/user/${userId}`, {
      params: {
        page,
        pageSize, 
        category
      }
    });
    console.log('获取用户资源列表:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法加载资源列表',
      type: 'error',
    });
    console.error('获取资源列表失败:', error.response?.data || error.message);
    return null;
  }
};

// 删除资源
const deleteResource = async (id) => {
  try {
    const response = await axiosClient.delete(`/resources/${id}`);
    console.log('资源删除成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法删除资源',
      type: 'error',
    });
    console.error('删除资源失败:', error.response?.data || error.message);
    return null;
  }
};

// 点赞资源
const likeResource = async (id) => {
  try {
    const response = await axiosClient.post(`/resources/${id}/like`);
    console.log('资源点赞成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法点赞资源',
      type: 'error',
    });
    console.error('资源点赞失败:', error.response?.data || error.message);
    return null;
  }
};

// 取消点赞
const unlikeResource = async (id) => {
  try {
    const response = await axiosClient.delete(`/resources/${id}/like`);
    console.log('取消点赞成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法取消点赞',
      type: 'error',
    });
    console.error('取消点赞失败:', error.response?.data || error.message);
    return null;
  }
};

// 评论资源
const commentOnResource = async (id, data) => {
  try {
    const response = await axiosClient.post(`/resources/${id}/comments`, data);
    console.log('评论成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法发表评论',
      type: 'error',
    });
    console.error('评论失败:', error.response?.data || error.message);
    return null;
  }
};

// 获取资源评论
const getResourceComments = async (id) => {
  try {
    const response = await axiosClient.get(`/resources/${id}/comments`);
    console.log('获取资源评论:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法加载评论',
      type: 'error',
    });
    console.error('获取评论失败:', error.response?.data || error.message);
    return null;
  }
};

const getResourceFavorites = async (page, pageSize, category) => {
  try {
    const response = await axiosClient.get(`/resources/favorite`, {
      params: {
       page, pageSize, category
      }
    });
    console.log('获取资源收藏:', response);
    return response; 
  } catch (error) {
    ElNotification({
      message: '无法加载收藏',
      type: 'error',
    }); 
  }
}

// 收藏资源
const favoriteResource = async (id) => {
  try {
    const response = await axiosClient.post(`/resources/${id}/favorite`);
    console.log('资源收藏成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法收藏资源',
      type: 'error',
    });
    console.error('资源收藏失败:', error.response?.data || error.message);
    return null;
  }
};

// 取消收藏
const unfavoriteResource = async (id) => {
  try {
    const response = await axiosClient.delete(`/resources/${id}/favorite`);
    console.log('取消收藏成功:', response);
    return response;
  } catch (error) {
    ElNotification({
      message: '无法取消收藏',
      type: 'error',
    });
    console.error('取消收藏失败:', error.response?.data || error.message);
    return null;
  }
};

const getRecommendResources = async()=>{
  try {
    const response = await axiosClient.get(`/resources/recommend`);
    console.log('获取推荐资源:', response);
    return response;
 } catch (error) {
    ElNotification({
      message: "获取推荐资源失败",
      type: 'error',
    });
 }
};

const getExternalImage = async (url) => {
  try {
    const response = await axiosClient.get(`/img-proxy`, {
      params: { url }
    });
    return response; 
  } catch (error) {
    return null;
  }
};

export default {
  createResource,
  getResourceDetail,
  getResourcesList,
  getUserResourcesList,
  deleteResource,
  likeResource,
  unlikeResource,
  commentOnResource,
  getResourceComments,
  getResourceFavorites,
  favoriteResource,
  unfavoriteResource,
  getRecommendResources,
  getExternalImage
};
