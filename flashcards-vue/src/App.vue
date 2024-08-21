<script setup>
  import { ref, computed } from 'vue'
  import Nav from './components/Nav.vue'
  import Login from './components/Login.vue'
</script>

<template>
  <div id="app">
    <main class="page-container">
      <header class="page-header">
        <Nav/>
        <h1 class="title">BlueMemory Flashcard App</h1>
      </header>
      <router-view />      
    </main>
  </div>
</template>

<style>
  @import url('https://fonts.googleapis.com/css?family=Fira+Sans:400,500,600,700,800');

  .nav-item {
    font-size: 1rem;
  }

  .page-container {
    margin-left: auto;
    margin-right: auto;
    
    padding-top: 1.5rem;
    padding-left: 1rem;
    padding-right: 1rem;
    overflow: hidden;
  }
  .page-header {
    padding-bottom: 40px;
    border-radius: 15px;
    transition: all .3s;
  }
  .title {
    font-size: 2.286rem;
    font-weight: $bold;
    text-align: center;
    margin-top: 5rem;
    margin-bottom: 3rem;
  }

  * {
    box-sizing: border-box;
  }

  body {
    min-height: 100vh;
    display: flex;
    font-weight: 400;
    font-family: 'Fira Sans', sans-serif;
  }

  h1, h2, h3, h4, h5, h6, label, span {
    font-weight: 500;
    font-family: 'Fira Sans', sans-serif;
  }

  body, html, #app, #root, .auth-wrapper {
    width: 100%;
    height: 100%;
  }

  #app {
    /*text-align: center; */
  }

  .navbar-light {
    background-color: #1C8EF9;
  }


</style>
