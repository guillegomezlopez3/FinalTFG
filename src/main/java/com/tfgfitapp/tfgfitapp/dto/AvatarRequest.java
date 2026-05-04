package com.tfgfitapp.tfgfitapp.dto;

public class AvatarRequest {
    private String avatar;

    public AvatarRequest() {}

    public AvatarRequest(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
