<template>
  <div class="auth-inner">
  <form @submit.prevent="handleLogin">
    <h3>Login</h3>

    <div class="form-group">
      <label>Username</label>
      <input type="text" class="form-control" placeholder="Username" v-model="username">
    </div>

    <div class="form-group">
      <label>Password</label>
      <input type="password" class="form-control" placeholder="Password" v-model="password">
    </div>

    <button class="btn btn-primary btn-block">Login</button>
    <div v-if="errorMessage" class="error-message">Error: {{ errorMessage }}</div>
  </form>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const store = useStore();
    const router = useRouter();
    const username = ref('');
    const password = ref('');
    const errorMessage = ref(null);

    const handleLogin = () => {
      	axios.post('http://localhost:8080/user/login', {
        	username: username.value,
        	password: password.value
      	})
      	.then(response => {
        	if (response.status === 200) {
          	console.log('Пользователь аутентифицирован');
          	// Вызываем экшен для входа пользователя и передаем данные пользователя
          	store.dispatch('login', response.data)
            	.then(() => {
              	router.push('/home');
            	})
            	.catch(error => {
              	console.error('Ошибка входа:', error);
            });
        	} else {
          		console.log('Не удалось аутентифицировать пользователя');
        	}
      	})
      	.catch(error => {
      	if (error.response.status === 401) {
      		errorMessage.value = "Wrong email or password";
      	} else {
      		console.log('Error', error);
      	}
      });
    };

    return {
      username,
      password,
      handleLogin,
      errorMessage
    };
  }
};
</script>

<style>
  
  label {
    margin-top: 5px;
  }

  h3 {
    text-align: center;
  }

  .btn {
    margin-top: 10px;
    padding: 10px;
    width: 100%;
    background-color: white;
    color: black;
  }

  .error-message {
    color: red;
  }

  .auth-inner {
    background-color: #1C8EF9;
    width: 450px;
    margin: auto;
    box-shadow: 0px 14px 80px rgba(34, 34, 58, 0.2);
    padding: 40px 55px 45px 55px;
    border-radius: 15px;
    transition: all .3s;
  }
</style>
