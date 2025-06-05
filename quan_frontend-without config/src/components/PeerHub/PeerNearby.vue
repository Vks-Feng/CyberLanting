<template>
    <div>
        <h1>用户位置</h1>
        
        <button class="ink-button" @click="handleFind">查找附近的人</button>

        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
        <!-- <div v-if="latitude && longitude">
            <p>纬度: {{ latitude }}</p>
            <p>经度: {{ longitude }}</p>
        </div> -->

        <el-scrollbar class="scrollbar">
        <div v-if="friendList.length > 0">
         
            <el-card class="card-container" v-for="friend in friendList" :key="friend.id">
                <div class="user-info">
                    <el-avatar :size="50" :src="friend.avatarUrl" />
                    <div class="user-details">
                        <p>{{ friend.nickname }} ({{ friend.username }})</p>
                        <p>Email: {{ friend.email }}</p>
                    </div>
                </div>

                <el-button v-if="!friend.isFriend" type="success" @click="addFriend(friend.id)" class="ink-button">
                    添加好友
                </el-button>

                <el-tag v-else type="info" class="friend-tag">已是好友/等待对方同意</el-tag>
            </el-card>
         
        </div>
      </el-scrollbar>

    </div>
</template>

<script setup>
import peerApi from '@/api/peer-api';
import { getUserInfo } from '@/api/userinfo-api';
import { ElNotification } from 'element-plus';
import { ref } from 'vue';

// 创建响应式数据
const latitude = ref(null);
const longitude = ref(null);
const errorMessage = ref(null);

const friendList = ref([]);

const handleFind = async () => {
    await getLocation();
    const response = await peerApi.getPeerNearby();
    friendList.value = response;
};

// 返回一个 Promise，等待经纬度获取成功
const getPosition = () => {
  return new Promise((resolve, reject) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;

          // 判断经纬度是否有效
          if (lat !== null && lon !== null && !isNaN(lat) && !isNaN(lon)) {
            resolve({ latitude: lat, longitude: lon });
          } else {
            reject('获取的经纬度无效');
          }
        },
        (error) => {
          reject(getErrorMessage(error.code));
        }
      );
    } else {
      reject('浏览器不支持地理位置功能');
    }
  });
};

// 获取用户位置的方法
const getLocation = async () => {
  try {
    const { latitude: lat, longitude: lon } = await getPosition();

    // 如果获取到有效的经纬度，则进行赋值和推送
    latitude.value = lat;
    longitude.value = lon;
    errorMessage.value = null; // 清空错误信息

    // 推送位置信息
    try {
      await peerApi.pushLocation(latitude.value, longitude.value);
    } catch (error) {
      console.error("位置信息推送失败", error);
    }

  } catch (error) {
    // 如果出现错误，显示通知
    console.error("获取位置信息失败", error);
    ElNotification.error({
      message: error,
    });
  }
};


// 根据错误码返回错误信息
const getErrorMessage = (errorCode) => {
  switch (errorCode) {
    case 1:
      return "用户拒绝访问地理位置";
    case 2:
      return "无法获取位置信息";
    case 3:
      return "请求超时";
    default:
      return "未知错误";
  }
};

//添加好友处理
const addFriend = async (id) => {
  try {

    if (id === getUserInfo().id) {
      ElNotification({
        message: "不能添加自己为好友",
        type: "error",
      });
      return;
    }

    else {
      const response = await peerApi.postAddFriend(id);

      const friednListresponse = await peerApi.getPeerNearby();
      friendList.value = friednListresponse;

      ElNotification({
        message: "发送好友申请成功",
        type: "success",
      });
      console.log("已发送好友申请", response);
    }
  } catch (error) {
    console.error("申请失败", error);
  }
};
</script>

<style scoped>
.error {
  color: red;
  font-size: 14px;
}

.card-container {
  margin-top: 20px;
}

.scrollbar {
  height: 75vh;

}

/* .user-info {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 8px;
} */
</style>
