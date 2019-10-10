package com.example.demo.controller.account;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class ChangeProfileForm implements Serializable {
  private static final long serialVersionUID = -2522286509834241791L;
  @Size(min = 0, max = 60)
  private String nickName;
  private MultipartFile avatarImage;
  private String deleteAvatarImage;

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public MultipartFile getAvatarImage() {
    return avatarImage;
  }

  public void setAvatarImage(MultipartFile avatarImage) {
    this.avatarImage = avatarImage;
  }

  public String getDeleteAvatarImage() {
    return deleteAvatarImage == null ? "" : deleteAvatarImage;
  }

  public void setDeleteAvatarImage(String deleteAvatarImage) {
    this.deleteAvatarImage = deleteAvatarImage;
  }

  @Override
  public String toString() {
    return "ChangeProfileForm{" +
        "nickName='" + nickName + '\'' +
        ", deleteAvatarImage='" + deleteAvatarImage + '\'' +
        '}';
  }
}
