package net.zengfx.integrationlayer.models;

import lombok.Data;

@Data
public class Person implements User, UserResponse {
    private int id;
    private String name;
    private String address;
    private String email;
    private String phone;
}
