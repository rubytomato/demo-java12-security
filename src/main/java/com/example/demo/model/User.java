package com.example.demo.model;

import com.example.demo.auth.UserRolesUtil;
import lombok.Data;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@ToString(exclude = {"password"})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name", length = 60, nullable = false)
  private String name;
  @Column(name = "email", length = 120, nullable = false, unique = true)
  private String email;
  @Column(name = "password", length = 255, nullable = false)
  private String password;
  @Column(name = "roles", length = 120)
  private String roles;
  @Column(name = "lock_flag", nullable = false)
  private Boolean lockFlag;
  @Column(name = "disable_flag", nullable = false)
  private Boolean disableFlag;
  @Column(name = "create_at", nullable = false)
  private LocalDateTime createAt;
  @Column(name = "update_at", nullable = false)
  private LocalDateTime updateAt;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile userProfile;

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
    userProfile.setUser(this);
  }

  public String getAvatarImageBase64Encode() {
    return this.userProfile.getAvatarImageBase64Encode();
  }

  @PrePersist
  private void prePersist() {
    this.lockFlag = Boolean.FALSE;
    this.disableFlag = Boolean.FALSE;
    this.createAt = LocalDateTime.now();
    this.updateAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updateAt = LocalDateTime.now();
  }

  public static User of(String name, String email, String encodedPassword, String[] roles) {
    return User.of(name, email, encodedPassword, roles, new UserProfile());
  }

  public static User of(String name, String email, String encodedPassword, String[] roles,
                        UserProfile userProfile) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPassword(encodedPassword);
    String joinedRoles = UserRolesUtil.joining(roles);
    user.setRoles(joinedRoles);
    user.setUserProfile(userProfile);
    return user;
  }

}
