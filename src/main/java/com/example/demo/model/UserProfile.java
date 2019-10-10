package com.example.demo.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "user_profile")
@Data
@ToString(exclude = {"user", "avatarImage", "avatarImageBase64Encode"})
public class UserProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "nick_name", length = 60)
  private String nickName;
  @Lob
  @Column(name = "avatar_image")
  private byte[] avatarImage;
  @Column(name = "create_at", nullable = false)
  private LocalDateTime createAt;
  @Column(name = "update_at", nullable = false)
  private LocalDateTime updateAt;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Transient
  private String avatarImageBase64Encode;

  public void setAvatarImage(byte[] avatarImage) {
    this.avatarImage = avatarImage;
    this.avatarImageBase64Encode = base64Encode();
  }

  String getAvatarImageBase64Encode() {
    return avatarImageBase64Encode == null ? "" : avatarImageBase64Encode;
  }

  private String base64Encode() {
    return new String(Base64.getEncoder().encode(avatarImage), StandardCharsets.US_ASCII);
  }

  @PostLoad
  private void init() {
    this.avatarImageBase64Encode = base64Encode();
  }

  @PrePersist
  private void prePersist() {
    this.avatarImage = new byte[0];
    this.createAt = LocalDateTime.now();
    this.updateAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updateAt = LocalDateTime.now();
  }

}
