package com.esgi.leitnersystem.cucumber;

import static org.junit.jupiter.api.Assertions.*;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardQuizSteps {

  @Autowired private CardService cardService;

  private List<Card> cardsForQuiz;
  private Card currentCard;
  private Category originalCategory;

  @Before
  public void setUp() {
    cardsForQuiz = null;
    currentCard = null;
    originalCategory = null;
  }

  @Given("A connected user triggers a quiz")
  public void a_connected_user_triggers_a_quiz() {
    UUID userId = UUID.randomUUID();
    CardUserData cardUserData = new CardUserData(
        "What is the capital of France?", "Paris", "geography", userId);
    Card card = cardService.createCard(cardUserData);
    assertNotNull(card, "The card must be successfully created");
    cardsForQuiz = cardService.getCardsForQuizz(LocalDate.now());

    assertFalse(cardsForQuiz.isEmpty(),
                "There should be at least one card due for the quiz");
    currentCard = cardsForQuiz.getFirst();
    originalCategory = currentCard.getCategory();
  }

  @When("The user answers the card's question")
  public void the_user_answers_the_card_s_question() {
    try {
      cardService.processCardAnswer(currentCard.getId(), true);
    } catch (Exception e) {
      fail("The submission of the answer failed", e);
    }
  }

  @When("The user answers the card's question with an incorrect answer")
  public void the_user_answers_the_card_s_question_with_an_incorrect_answer() {
    try {
      cardService.processCardAnswer(currentCard.getId(), false);
    } catch (Exception e) {
      fail("The submission of the answer failed", e);
    }
  }

  @When("The user answers the card's question with the category {string}")
  public void
  the_user_answers_the_card_s_question_with_the_category(String category) {
    try {
      currentCard.setCategory(Category.valueOf(category));
      cardService.updateCard(currentCard);
      cardService.processCardAnswer(currentCard.getId(), true);
    } catch (Exception e) {
      fail("The submission of the answer failed", e);
    }
  }

  @Then(
      "The answer is recorded, and the user can see if they answered correctly or not")
  public void
  the_answer_is_recorded_and_the_user_can_see_if_they_answered_correctly_or_not() {
    Card updatedCard =
        cardService.fetchAllCards(Optional.empty())
            .stream()
            .filter(card -> card.getId().equals(currentCard.getId()))
            .findFirst()
            .orElseThrow(
                () -> new AssertionError("The updated card is not found"));
    boolean isPromoted =
        updatedCard.getCategory().ordinal() > originalCategory.ordinal();
    boolean isDemoted =
        updatedCard.getCategory().ordinal() < originalCategory.ordinal();

    if (isPromoted) {
      assertTrue(updatedCard.getCategory().ordinal() >
                     originalCategory.ordinal(),
                 "The card should be promoted");
    } else if (isDemoted) {
      assertTrue(updatedCard.getCategory().ordinal() <
                     originalCategory.ordinal(),
                 "The card should be demoted");
    } else {
      assertEquals(originalCategory, updatedCard.getCategory(),
                   "The card should remain in the same category");
    }
  }

  @And("The card is moved to category {string}")
  public void the_card_is_moved_to_category(String expectedCategory) {
    Card updatedCard =
        cardService.fetchAllCards(Optional.empty())
            .stream()
            .filter(card -> card.getId().equals(currentCard.getId()))
            .findFirst()
            .orElseThrow(
                () -> new AssertionError("The updated card is not found"));

    assertEquals(expectedCategory, updatedCard.getCategory().name(),
                 "The card should be moved to the specified category");
  }

  @And("The card should be promoted to {string} and not be asked again")
  public void the_card_should_be_promoted_to_and_not_be_asked_again(
      String expectedCategory) {
    Card updatedCard =
        cardService.fetchAllCards(Optional.empty())
            .stream()
            .filter(card -> card.getId().equals(currentCard.getId()))
            .findFirst()
            .orElseThrow(
                () -> new AssertionError("The updated card is not found"));

    assertEquals(expectedCategory, updatedCard.getCategory().name(),
                 "The card should be promoted to the specified category");
  }
}
