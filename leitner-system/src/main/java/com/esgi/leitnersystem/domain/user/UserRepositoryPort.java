package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> login(String username, String password);
  List<UserEntity> getAllUsers();
  UserEntity save(UserEntity user);
  void deleteUserById(UUID id);
}
