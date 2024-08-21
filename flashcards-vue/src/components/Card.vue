<template>
  <div class="flashcard">
    <div @click="toggleSide">
      <div class="card-front" v-if="showFront">
        <p>{{ card.question }}</p>
        <p class="tag">{{ card.tag }}</p>
      </div>
      <div class="card-back" v-else>
        <p>{{ card.answer }}</p>
        <p class="tag">Repeat after {{ getCategoryRepeatTime(card.category) }} days</p>
      </div>
    </div>
        <button class="delete-icon" @click.stop="deleteCard(card.id)">
      <i class="fas fa-trash"></i>
    </button>
  </div>
</template>
<script>
import axios from 'axios';
import { ref, defineComponent, getCurrentInstance, onMounted} from 'vue';
import '@fortawesome/fontawesome-free/css/all.css'; // импорт всех иконок


export default {
  props: ['card'],
  setup(props, { emit }) {
    const showFront = ref(true);

    const toggleSide = () => {
      showFront.value = !showFront.value;
    };

    const deleteCard = (cardId) => {
      console.log(cardId);
      axios.delete(`http://localhost:8080/cards/${cardId}`)
        .then(response => {
          if (response.status === 204) {
            console.log("Card deleted successfully");
            emit('cardDeleted');
          } else {
            console.log("Failed to delete card");
          }
        })
        .catch(error => {
          console.error("Error deleting card:", error);
        });
    };

    const getCategoryRepeatTime = (category) => {
      switch (category) {
        case 'FIRST':
          return 1;
        case 'SECOND':
          return 2;
        case 'THIRD':
          return 3;
        case 'FOURTH':
          return 8;
        case 'FIFTH':
          return 16;
        case 'SIXTH':
          return 32;
        case 'SEVENTH':
        case 'DONE':
          return 'Выучено!';
        default:
          return '';
      }
    };

    return {
      showFront,
      toggleSide,
      deleteCard,
      getCategoryRepeatTime
    };
  }
};
</script>


<style>

.delete-icon {
  top:15px;
  right: 15px;
  width: 25px;
  border: 0;
  background-color: transparent;
  position: absolute;
  padding: 0;
}

.flashcard {
  width: 250px;
  height: 335px; /* Set a fixed height for the card */
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 80px 30px;
  cursor: pointer;
  text-align: center;
  position: relative;
  transition: transform 0.3s; /* Add transition for smooth flip effect */
  backface-visibility: hidden; /* Ensure backface is not visible until flipped */
  background-color: #1C8EF9;
  color: white;
}

.card-front, .card-back {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-weight: 300;
  font-size: 1.2rem;
}

.tag {
  margin-top: 10px;
  font-style: italic;
  font-size: 17px;
}
</style>
