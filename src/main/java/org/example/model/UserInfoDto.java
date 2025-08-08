package org.example.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.entities.UserInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

    private String username;

    private String lastname;

    private Long phoneNumber;

    private String email;

}
