import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Home from './components/Home.vue'
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import Study from './components/Study.vue'
import store from './store.js'

const routes = [
  { path: '/home', component: Home },
  { path: '/login', component: Login },
  { path: '/register', component: Register},
  { path: '/study', component: Study}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

createApp(App).use(router).use(store).mount('#app')
