<template>
  <div class="auth-inner">
  <form @submit.prevent="handleRegister">
    <h3>Register</h3>

    <div class="form-group">
      <label>Username</label>
      <input type="text" class="form-control" placeholder="Username" v-model="username">
    </div>

    <div class="form-group">
      <label>Password</label>
      <input type="password" class="form-control" placeholder="Password" v-model="password">
    </div>

    <button type="submit" class="btn btn-primary btn-block">Sign up</button>
    <div v-if="errorMessage">
      <p class="error-message">{{ errorMessage }}</p>
    </div>
  </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { minidenticonSvg } from 'minidenticons';

export default {
  setup() {
    const store = useStore();
    const router = useRouter();
    const username = ref('');
    const password = ref('');
    const errorMessage = ref(null);

    const handleRegister = () => {
      axios.post('http://localhost:8080/user/register', {
        username: username.value,
        password: password.value
      })
      .then(response => {
        if (response.status === 201) {
          console.log('Пользователь зарегистрирован');
          // Вызываем экшен для входа пользователя и передаем данные пользователя
          store.dispatch('login', response.data)
            .then(() => {
              router.push('/home');
            })
            .catch(error => {
              console.error('Ошибка входа:', error);
            });
        } else {
          console.log('Не удалось зарегистрировать пользователя');
        }
      })
      .catch(error => {
          if (error.response.status === 409) {
          errorMessage.value = "User already exist";
        } else {
          console.log('Error', error);
        }
      });
    };

    return {
      username,
      password,
      handleRegister,
      errorMessage
    };
  }
};

</script>

<style>
      .auth-inner {
    width: 450px;
    margin: auto;
    box-shadow: 0px 14px 80px rgba(34, 34, 58, 0.2);
    padding: 40px 55px 45px 55px;
    border-radius: 15px;
    transition: all .3s;
  }
</style>