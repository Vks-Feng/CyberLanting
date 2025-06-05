import { createStore } from 'vuex'

const store = createStore({
  state() {
    return {
      count: 0,
      objList: null,
      todayTaskList: null,
      question: null,
      aiRequirement: null,

      userInfos:{
        id: null,
        nickname:'Guest',
        email: null,
        avatar_url: null,
      },
    }
  },

  mutations: {
    increment(state) {
      state.count++
    },

    setObjList(state, objList) {
      state.objList = objList
    },

    setTodayTaskList(state, taskList) {
      state.todayTaskList= taskList
    },

    setQuestion(state, question) {
      state.question= question
    },
    clearQuestion(state) {
      state.question = []
    },

    setAiRequirement(state, aiRequirement) {
      state.aiRequirement = aiRequirement
    },
    clearAiRequirement(state) {
      state.aiRequirement = null
    },
    setUserInfos(state, userInfos) {
      state.userInfos = userInfos
    },

    // 更新日期状态
    updateCurrentDate(state, { year, month }) {
      state.currentDate = { year, month }
    }
  },


  actions: {
    //获取用户信息
    async fetchUserInfo({ commit }) {
      try {
        const response = await userInfoApi.getUserInfo();
        commit('setUserInfo',response.data);
      }catch(error){
        console.error('Failed to fetch UserInfo :',error);
      }      
    },
  }
}
);

export default store