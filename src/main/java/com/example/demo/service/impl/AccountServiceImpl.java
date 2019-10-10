package com.example.demo.service.impl;

import com.example.demo.auth.SimpleLoginUser;
import com.example.demo.auth.UserRolesUtil;
import com.example.demo.model.User;
import com.example.demo.model.UserProfile;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AccountServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @Transactional
  @Override
  public void register(String name, String email, String rawPassword, String[] roles) {
    log.debug("user register name:{}, email:{}, roles:{}", name, email, roles);
    String encodedPassword = passwordEncoder.encode(rawPassword);
    User storedUser = userRepository.saveAndFlush(User.of(name, email, encodedPassword, roles));
    authentication(storedUser, rawPassword);
  }

  @Transactional
  @Override
  public void changePassword(User user, String newRawPassword) {
    log.debug("change password user:{}", user);
    User findUser = findUser(user.getId());
    String encodedPassword = passwordEncoder.encode(newRawPassword);
    findUser.setPassword(encodedPassword);
    User storedUser = userRepository.saveAndFlush(findUser);
    authentication(storedUser, newRawPassword);
  }

  @Transactional
  @Override
  public void changeRole(User user, String[] roles) {
    log.debug("change role user:{}, roles:{}", user, roles);
    String joinedRoles = UserRolesUtil.joining(roles);
    User findUser = findUser(user.getId());
    findUser.setRoles(joinedRoles);
    User storedUser = userRepository.saveAndFlush(findUser);
    updateAuthorities(storedUser);
  }

  @Transactional
  @Override
  public void changeProfile(User user, String nickName, byte[] avatarImage, String deleteAvatarImage) {
    log.debug("change profile user:{}", user);
    userRepository.findById(user.getId())
        .ifPresentOrElse(findUser -> {
              UserProfile userProfile = findUser.getUserProfile();
              userProfile.setNickName(nickName);
              if (deleteAvatarImage.equals("delete")) {
                userProfile.setAvatarImage(new byte[0]);
              } else if (avatarImage.length > 0) {
                  userProfile.setAvatarImage(avatarImage);
              }
              findUser.setUserProfile(userProfile);
              userRepository.saveAndFlush(findUser);
              updateProfile(userProfile);
            },
            () -> {
              log.error("user not found:{}", user);
            });
  }

  @Transactional
  @Override
  public void delete(final User user) {
    log.debug("delete user:{}", user);
    userRepository.findById(user.getId())
        .ifPresentOrElse(findUser -> {
              userRepository.delete(findUser);
              userRepository.flush();
            },
            () -> {
              log.error("user not found:{}", user);
            });
  }

  @Transactional(readOnly = true)
  @Override
  public boolean exists(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  private User findUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found. userId:" + userId));
  }

  private void authentication(User user, String rawPassword) {
    log.debug("authenticate user:{}", user);
    SimpleLoginUser loginUser = new SimpleLoginUser(user);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, rawPassword, loginUser.getAuthorities());
    log.debug("auth token:{}", authenticationToken);
    Authentication auth = authenticationManager.authenticate(authenticationToken);
    if (authenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    } else {
      throw new RuntimeException("authenticate failure user:[" + user.toString() + "]");
    }
  }

  private void updateAuthorities(User user) {
    log.debug("update authorities user:{}", user);
    Set<GrantedAuthority> updatedAuthorities = UserRolesUtil.toSet(user.getRoles());
    SimpleLoginUser loginUser = new SimpleLoginUser(user);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, authentication.getCredentials(), updatedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  private void updateProfile(UserProfile userProfile) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    SimpleLoginUser loginUser = (SimpleLoginUser) authentication.getPrincipal();
    loginUser.getUser().setUserProfile(userProfile);
  }

}
