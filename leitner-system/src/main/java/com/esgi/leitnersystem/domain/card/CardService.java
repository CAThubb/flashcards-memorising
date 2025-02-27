package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.Quiz.QuizService;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.domain.revision.RevisionService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import com.esgi.leitnersystem.infrastructure.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import com.esgi.leitnersystem.presentation.controller.CardsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
  private final CardRepositoryPort cardRepository;
  private final CategoryService categoryService;
  private final QuizService quizService;
  private final RevisionService revisionService;

  private final UserRepository userRepository;

  @Autowired
  public CardService(CardRepositoryPort cardRepository,
                     CategoryService categoryService, QuizService quizService,
                     RevisionService revisionService, UserRepository userRepository) {
    this.cardRepository = cardRepository;
    this.categoryService = categoryService;
    this.quizService = quizService;
    this.revisionService = revisionService;
    this.userRepository = userRepository;
  }

  public Card createCard(CardUserData cardUserData) {
    UserEntity user = userRepository.findByUserId(cardUserData.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    var card = Card.builder()
            .question(cardUserData.getQuestion())
            .answer(cardUserData.getAnswer())
            .tag(cardUserData.getTag())
            .category(Category.FIRST)
            .user(user)
            .build();

    return cardRepository.save(card);
  }

  public List<Card> fetchAllCards(Optional<List<String>> tags) {
    return tags
        .map(
            t
            -> t.stream().map(String::toLowerCase).collect(Collectors.toList()))
        .map(cardRepository::findByTagsIn)
        .orElseGet(cardRepository::findAll);
  }

  public List<Card> getCardsForQuizz(LocalDate date) {
    return quizService.getCardsDueForQuiz(date);
  }

  @Transactional
  public void processCardAnswer(UUID cardId, boolean isValid)
      throws CardNotFoundException {
    var card = cardRepository.findById(cardId).orElseThrow(
        ()
            -> new CardNotFoundException("Card with ID " + cardId +
                                         " not found"));

    if (isValid) {
      categoryService.promoteCard(card);
      if (card.getCategory() == Category.DONE) {
        markCardAsDone(card);
      }
    } else {
      categoryService.demoteCardToFirst(card);
    }

    recordCardRevision(card, isValid);
  }

  private void markCardAsDone(Card card) {
    card.setCategory(Category.DONE);
    cardRepository.save(card);
  }

  private void recordCardRevision(Card card, boolean isValid) {
    revisionService.recordCardRevision(card, isValid);
  }

  public void updateCard(Card currentCard) { cardRepository.save(currentCard); }

  public void deleteCardById(UUID cardId) {
    cardRepository.deleteCardById(cardId);
  }
}
