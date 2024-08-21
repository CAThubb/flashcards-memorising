<template>
  <div>
    <div class="top-message">
      <p class="cardcount">You have {{ filteredCardsCount }} cards so far, {{ username }}</p> 
      <button class="btn-newcard" @click="showForm = true">Add new card!</button> 
    </div>
    <div v-if="showForm" class="new-card-form">
      <input type="text" v-model="question" placeholder="Enter question" class="input-card">
      <input type="text" v-model="answer" placeholder="Enter answer" class="input-card">
      <input type="text" v-model="tag" placeholder="Enter tag" class="input-card">
      <button class="btn-newcard" @click="createCard">Create</button>
      <button class="btn-newcard" @click="showForm = false">Close</button> <!-- Добавляем кнопку для закрытия формы -->
    </div>
    <div class="card-box">
      <ul class="card-list">
        <li v-for="card in filteredCards" :key="card.id">
          <Card :card="card" @cardDeleted="reloadCards"/>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import Card from './Card.vue'
import axios from 'axios';
import { onMounted, ref, computed } from 'vue';

export default {
  components: {
    Card
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const username = store.state.userData ? store.state.userData.username : 'Guest';
    let filteredCards = ref(null);
    
    let showForm = ref(false);
    const question = ref('');
    const answer = ref('');
    const tag = ref('');

    const userId = ref('');

    if (username === 'Guest') {
      router.push('/login');
    }
    const getCards = () => {
      axios.get("http://localhost:8080/cards")
      .then(response => {
        if (response.status === 200) {
          filteredCards.value = response.data.filter(card => card.user.username === username);
          console.log(filteredCards)
        } else {
          console.log("Cards not fetched");
        }
      }).catch(error => {
        console.log("Error", error);
      });
    }

    const createCard = () => {
    console.log('func to create card');

    axios.get("http://localhost:8080/user")
    .then(usersResponse => {
      // Find the user with the specified username
      const user = usersResponse.data.find(user => user.username === username);
      userId.value = user.userId;
      if (!user) {
        console.error("User not found");
        return;
    }

    const newCardData = {
      question: question.value,
      answer: answer.value,
      tag: tag.value,
      userId: userId.value // Используйте userId.value, чтобы получить фактическое значение
    };
    console.log(newCardData);

      axios.post("http://localhost:8080/cards", newCardData)
      .then(response => {
        if (response.status === 201) {
          console.log("Card created");
          showForm.value = false;
          question.value = '';
          answer.value = '';
          tag.value = '';
          userId.value = '';
          reloadCards();
        } else {
          console.log('Failed to create card');
        }
        }).catch(error => {
          console.error("Error creating card: ", error);
        });
      });
    }; // Закрываем функцию createCard

    const reloadCards = () => {
      getCards();
    };

    const filteredCardsCount = computed(() => {
      return filteredCards.value ? filteredCards.value.length : 0;
    });

    onMounted(() => {
      getCards(); // Call getCards function when the component is mounted
    });

    return {
      reloadCards,
      filteredCards,
      username,
      filteredCardsCount,
      showForm,
      createCard,
      answer,
      tag,
      question,
    }
  }
};
</script>

<style>

  .input-card {
    margin: 10px;
  }

  .btn-newcard {
    padding: 6px;
    border-radius: 5px;
    font-weight: 500;
    background-color: #1C8EF9;
    margin-top: 10px;
  }

  .cardcount {
    font-size: 1.143rem;
    font-weight: 500;
    text-align: center;
  }
  .card-box {
    display: flex;
    justify-content: center;
  }

  .card-list {
    list-style: none;
    padding: 0;
  }

  .card-list > li { /* Set the width of each card. Adjust as needed */
    display: inline-block;
    margin: 10px;
  }
  .top-message {
    display: flex;
    flex-direction: column; /* Arrange items vertically */
    align-items: center;

  }

  .new-card-form {
    text-align: center;
  }

</style>