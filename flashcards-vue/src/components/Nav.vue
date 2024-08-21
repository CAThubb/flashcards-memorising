<template>
  <nav class="navbar navbar-expand navbar-light fixed-top">
    <div class="container">
      <router-link to="home" class="navbar-brand">Home</router-link>
      <div class="collapse navbar-collapse">
        <ul v-if="!isAuthenticated" class="navbar-nav ml-auto">
          <!-- Conditionally render "Login" and "Sign up" if user is not authenticated -->
          <li class="nav-item">
            <router-link to="login" class="nav-link">Login</router-link>
          </li>
          <li class="nav-item">
            <router-link to="register" class="nav-link">Sign up</router-link>
          </li>
        </ul>
        <ul v-else class="navbar-nav ml-auto">
          <li class="nav-item">
            <router-link to="study" class="nav-link">Study</router-link>
          </li>
          <!-- Render "Logout" if user is authenticated -->
          <li class="nav-item">
            <button @click="handleSignOut" class="nav-link">Logout</button>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { computed, ref } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const store = useStore();
    const router = useRouter();
    const isAuthenticated = computed(() => store.getters.isAuthenticated);
    const handleSignOut = () => {
      store.dispatch('logout')
        .then(() => {
          // После успешного выхода перенаправляем на страницу входа
          router.push('/login');
        })
        .catch(error => {
          console.error('Ошибка выхода:', error);
        });
    };

    return {
      isAuthenticated,
      handleSignOut,
    };
  }
};

</script>

<style>
</style>