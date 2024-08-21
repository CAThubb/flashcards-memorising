<template>
  <div v-if="filteredQuizz" class="flashcard-wrapper">
    <div class="flashcard" @click="toggleCard">
      <div v-if="!showAnswer && currentCardIndex < filteredQuizz.length">
        <h2>{{ filteredQuizz[currentCardIndex].question }}</h2>
        <h4 class="tag">{{ filteredQuizz[currentCardIndex].tag }}</h4>
      </div>
      <div v-else-if="showAnswer && currentCardIndex < filteredQuizz.length">
        <h2>{{ filteredQuizz[currentCardIndex].answer }}</h2>
      </div>
      <div v-else>
        <p>Congratulations! You have completed the quiz.</p>
      </div>
    </div>
    <div class="buttons">
      <button class="study" @click="cardKnown">I know</button>
      <button class="study" @click="cardUnknown">I don't know</button>
    </div>
  </div>
</template>



<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import axios from 'axios';
import { onMounted, ref } from 'vue';

export default {
  setup() {
    const store = useStore();
    const router = useRouter();

    const currentCardIndex = ref(0);
    const username = store.state.userData ? store.state.userData.username : 'Guest';
    let filteredQuizz = ref(null);
    const showAnswer = ref(false); // Добавляем состояние для отслеживания открытия ответа

    const currentDate = new Date();
    const year = currentDate.getFullYear();
    // Месяц начинается с 0, поэтому добавляем 1
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const day = currentDate.getDate().toString().padStart(2, '0');
    
    const formattedDate = `${year}-${month}-${day}`;
    
    const url = `http://localhost:8080/cards/quizz?date=${formattedDate}`;
    console.log(url);

    if (username === 'Guest') {
      router.push('/login');
    }

    const getQuizz = () => {
      axios.get(url)
        .then(response => {
          if (response.status === 200) {
            console.log("fetched quizz data");
            filteredQuizz.value = response.data.filter(card => card.user.username === username);
          } else {
            console.log("Quizz is not fetched");
          }
        }).catch(error => {
          console.log("Error", error);
        });
    }

    const cardKnown = () => {
    if (currentCardIndex.value < filteredQuizz.value.length - 1) {
      currentCardIndex.value++;
      showAnswer.value = false; // Скрыть ответ после перехода на следующую карточку
      const cardId = filteredQuizz.value[currentCardIndex.value].id;
      patchAnswer(cardId, true);
    } else {
      showAnswer.value = false; // Скрыть ответ после завершения викторины
      alert('Congratulations! You have completed the quiz.'); // Вывод сообщения о завершении викторины
      router.push('/home');
    }
  }

  const cardUnknown = () => {
    if (currentCardIndex.value < filteredQuizz.value.length - 1) {
      currentCardIndex.value++;
      showAnswer.value = false; // Скрыть ответ после перехода на следующую карточку
      const cardId = filteredQuizz.value[currentCardIndex.value].id;
      patchAnswer(cardId, false);
    } else {
      showAnswer.value = false; // Скрыть ответ после завершения викторины
      alert('Congratulations! You have completed the quiz.'); // Вывод сообщения о завершении викторины
      router.push('/home');
    }
  }

  const patchAnswer = (cardId, isValid) => {
    axios.patch(`http://localhost:8080/cards/${cardId}/answer`, {
      isValid: isValid
    })
    .then(response => {
      console.log('PATCH request successful');
      nextCard();
    })
    .catch(error => {
      console.error('Error in PATCH request:', error);
    });
  }

  const toggleCard = () => {
      showAnswer.value = !showAnswer.value; // Переключение состояния открытия ответа
    }

  const nextCard = () => {
      if (currentCardIndex.value < filteredQuizz.value.length - 1) {
        currentCardIndex.value++;
        showAnswer.value = false; // Скрыть ответ после перехода на следующую карточку
      } else {
        showAnswer.value = false; // Скрыть ответ после завершения викторины
      }
    }

    onMounted(() => {
      getQuizz(); // Call getQuizz function when the component is mounted
    });

    return {
      filteredQuizz,
      username,
      currentCardIndex,
      showAnswer,
      cardKnown,
      cardUnknown,
      toggleCard,
      nextCard
    };
  }
}
</script>

<style>
	.flashcard-wrapper {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.buttons {
		margin: 5px;

	}
	.study {
		margin: 5px;
		padding: 5px;
		border-radius: 5px;
    	font-weight: 500;
    	background-color: #1C8EF9;
    	color: white;
    	width: 110px;
	}
</style>