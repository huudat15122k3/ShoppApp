package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    private String address;
    @NotBlank(message = "Password can not be blank!")
    private String password;
    public String getPassword() {
        return password;
    }

    @JsonProperty("retype_password")
    private String retypePassword;
    public String getRetypePassword() {
        return retypePassword;
    }


    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "Role ID is required!")
    @JsonProperty("role_id")
    private long roleId;
}
