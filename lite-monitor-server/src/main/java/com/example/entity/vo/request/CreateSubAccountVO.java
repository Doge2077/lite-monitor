package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateSubAccountVO {
    @Length(min = 1, max = 10)
    String username;
    @Email
    String email;
    @Length(min = 6, max = 20)
    String password;
    @Size(min = 1)
    List<Integer> clients;
}
