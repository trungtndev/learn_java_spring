package com.learnweb.learnweb.dto.request;

import com.learnweb.learnweb.entity.User;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_UNVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_UNVALID")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
