package com.esgi.leitnersystem.presentation.controller;

import com.esgi.leitnersystem.domain.user.User;
import com.esgi.leitnersystem.domain.user.UserService;
import com.esgi.leitnersystem.infrastructure.dto.UserLoginDto;
import com.esgi.leitnersystem.infrastructure.dto.UserRegisterDto;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  @Tag(name = "User")
  @Operation(
      summary = "Log in the user",
      description =
          "Used to log in a registered user. The login endpoint will work only if a user with the same username and password is already registered")
  @ApiResponse(responseCode = "200",
               description = "User successfully logged in",
               content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = User.class))
               )
  @ApiResponse(responseCode = "401", description = "Unauthorized",
               content = @Content())
  public ResponseEntity<User>
  login(@RequestBody UserLoginDto loginBody) {
    UserEntity authenticated =
        userService.login(loginBody.getUsername(), loginBody.getPassword());
    User user =
        new User(authenticated.getUsername(), authenticated.getPassword());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/register")
  @Tag(name = "User")
  @Operation(
      summary = "Create a new user",
      description =
          "Used to create a new user in the system. This new user will be able to login with the login endpoint.")
  @ApiResponse(responseCode = "201", description = "User successfully created",
               content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = User.class))
               )
  @ApiResponse(responseCode = "400", description = "Bad request",
               content = @Content())
  public ResponseEntity<?> register(@RequestBody
                                    @Valid UserRegisterDto registerBody) {
    try {
      User createdUser = userService.register(registerBody.getUsername(),
                                              registerBody.getPassword());

      Logger.getLogger(UserController.class.getName())
          .log(Level.INFO, "User created: " + createdUser);
      return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    } catch (Exception e) {
      Logger.getLogger(UserController.class.getName())
          .log(Level.SEVERE, "Error creating the user", e);
      return ResponseEntity.badRequest().body("Error creating the user: " +
                                              e.getMessage());
    }
  }

  @DeleteMapping("/{userId}")
  @Tag(name = "User")
  @Operation(
          summary = "Delete a user",
          description = "Delete a user by their ID"
  )
  @ApiResponse(responseCode = "204", description = "User successfully deleted")
  @ApiResponse(responseCode = "404", description = "User not found")
  public ResponseEntity<?> deleteUser(@PathVariable("userId") UUID userId) {
    try {
      userService.deleteUserById(userId);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      Logger.getLogger(UserController.class.getName())
              .log(Level.SEVERE, "Error deleting the user", e);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping()
  @Tag(name = "User")
  @Operation(
          summary = "Get all users",
          description = "Get a list of all users"
  )
  @ApiResponse(responseCode = "200", description = "List of users retrieved",
          content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UserEntity.class)))
  public ResponseEntity<List<UserEntity>> getAllUsers() {
    List<UserEntity> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}
