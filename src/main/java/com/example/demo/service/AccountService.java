package com.example.demo.service;

import com.example.demo.model.User;

public interface AccountService {
  void register(String name, String email, String rawPassword, String[] roles);
  void changePassword(User user, String newRawPassword);
  void changeRole(User user, String[] roles);
  void changeProfile(User user, String nickName, byte[] avatarImage, String deleteAvatarImage);
  void delete(User user);
  boolean exists(String email);
}
