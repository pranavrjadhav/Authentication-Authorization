package com.zenton.auth.Authorization.controller;

import com.zenton.auth.Authorization.dtos.Admindtos.UserGetAllDto;
import com.zenton.auth.Authorization.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminpanel/")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/getalluser")
    public ResponseEntity<Page<UserGetAllDto>> getAllUser(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String username){
        return ResponseEntity.ok(
                adminService.getAllUsers(page,size,username)
        );
    }

}
