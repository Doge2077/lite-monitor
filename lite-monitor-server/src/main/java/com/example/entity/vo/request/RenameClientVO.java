package com.example.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
public class RenameClientVO {
    @NotNull
    Integer clientId;
    @Length(min = 1, max = 20)
    String clientName;
}
