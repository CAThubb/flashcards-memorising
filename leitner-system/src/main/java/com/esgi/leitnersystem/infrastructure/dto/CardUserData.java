package com.esgi.leitnersystem.infrastructure.dto;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Schema(name = "CardUserData", description = "Data to create a new card")
@AllArgsConstructor
@RequiredArgsConstructor
public class CardUserData {

  @NotBlank(message = "Question is mandatory")
  @Schema(description = "Question to be asked to the user during a quizz",
          example = "What is pair programming?", required = true)
  private String question;

  @NotBlank(message = "Answer is mandatory")
  @Schema(description = "Expected answer for the question",
          example = "A practice to work in pair on the same computer.",
          required = true)
  private String answer;

  @Schema(description = "A tag to group cards on the same topic",
          example = "Teamwork")
  private String tag;

  @NotNull(message = "Userid is mandatory")
  @Schema(description = "Expected userId for flashcard",
          example = "6c10ad48-2bb8-4e2e-900a-21d62c00c07b",
          required = true)
  private UUID userId;
}
