package com.esgi.leitnersystem.infrastructure.repository;

import com.esgi.leitnersystem.domain.card.Card;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
  @Query("SELECT c FROM Card c WHERE LOWER(c.tag) IN :tags")
  List<Card> findByTagsIn(List<String> tags);

  Collection<Card> findAllByIdNotIn(List<UUID> cardIdsRevisedToday);

  @Transactional
  void deleteCardById(UUID cardId);
}
