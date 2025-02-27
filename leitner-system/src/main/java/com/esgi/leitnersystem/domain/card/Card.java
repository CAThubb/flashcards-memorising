package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  @Schema(description = "Generated identifier of a card",
          example = "6c10ad48-2bb8-4e2e-900a-21d62c00c07b", required = true)
  private UUID id;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(
      description =
          "Category of card indicating how many times you answered it and appearance frequency",
      example = "FIRST", required = true)
  private Category category;

  @Column(nullable = false)
  @Schema(description = "Question to be asked to the user during a quiz",
          example = "What is pair programming?", required = true)
  private String question;

  @Column(nullable = false)
  @Schema(description = "Expected answer for the question",
          example = "A practice to work in pairs on the same computer.",
          required = true)
  private String answer;

  @Schema(description = "A tag to group cards on the same topic",
          example = "Teamwork")
  private String tag;
  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private UserEntity user;
}
