package com.maksympanov.hneu.mjt.sbcrud.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersInfoDto {

    private List<UserInfoDto> users;

}
