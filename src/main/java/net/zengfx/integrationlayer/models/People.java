package net.zengfx.integrationlayer.models;

import lombok.Data;

@Data
public class People implements User, UserResponse {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
}
