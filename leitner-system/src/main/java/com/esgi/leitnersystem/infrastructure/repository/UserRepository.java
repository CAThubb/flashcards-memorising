package com.esgi.leitnersystem.infrastructure.repository;

import com.esgi.leitnersystem.domain.user.User;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  @Query(
      "SELECT user FROM UserEntity user WHERE user.username = :username AND user.password = :password")
  UserEntity
  login(String username, String password);

  @Modifying
  @Transactional
  @Query("DELETE FROM UserEntity user WHERE user.userId = :userId")
  void deleteUserById(UUID userId);

  @Query("SELECT user FROM UserEntity user WHERE user.userId = :userId")
  Optional<UserEntity> findByUserId(UUID userId);

  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findAll();
}
