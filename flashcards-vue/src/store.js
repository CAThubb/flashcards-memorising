import { createStore } from 'vuex';

const store = createStore({
  state() {
    return {
      userData: null
    };
  },
  mutations: {
    setUserData(state, userData) {
      state.userData = userData;
    },
    clearUserData(state) {
      state.userData = null;
    }
  },
  actions: {
    login({ commit }, userData) {
      // Вы можете выполнить здесь ваш запрос к серверу для входа пользователя
      // После успешного входа вызовите мутацию setUserData
      commit('setUserData', userData);
    },
    logout({ commit }) {
      // Выполните здесь запрос к серверу для выхода пользователя, если необходимо
      // После успешного выхода вызовите мутацию clearUserData
      commit('clearUserData');
    }
  },
  getters: {
    isAuthenticated(state) {
      return !!state.userData;
    }
  }
});

export default store;