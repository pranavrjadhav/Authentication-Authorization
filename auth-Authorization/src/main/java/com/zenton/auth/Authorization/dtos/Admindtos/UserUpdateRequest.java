package com.zenton.auth.Authorization.dtos.Admindtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private Long id;
    private String username;
    List<Integer> roleId;
}
