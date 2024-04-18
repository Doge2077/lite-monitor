package com.example.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
public class SshConnectionVO {
    @NonNull
    Integer clientId;
    @NonNull
    Integer port;
    @NonNull
    @Length(min = 1)
    String username;
    @NotNull
    @Length(min = 1)
    String password;

}
