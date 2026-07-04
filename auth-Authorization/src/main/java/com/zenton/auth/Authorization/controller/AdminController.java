package com.zenton.auth.Authorization.controller;

import com.zenton.auth.Authorization.dtos.Admindtos.*;
import com.zenton.auth.Authorization.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminpanel/")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //totalPages =
    //    (totalRecords + size - 1) / size;

    //offset = (page) * size;
    //exp:-- size = 10;
    //Page 0 -> 1-10
    //Page 1 -> 11-20
    //...
    //Page 9 -> 91-95

    @GetMapping("/getalluser")
    public ResponseEntity<PageResponse> getAllUser(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String username){
        return ResponseEntity.ok(
                adminService.getAllUsers(page,size,username)
        );
    }

//    get user by id/name
    @GetMapping("/getbyuserid/{userid}")
    public ResponseEntity<UserGetByIdDto> getUserById(@PathVariable Long userid){
        return ResponseEntity.ok(adminService.getUserById(userid));
    }


    @GetMapping("/getAllRoles&Permission")
    public  ResponseEntity<ListOfRolesAndPermission> getAllRolesPermission(){
         return  ResponseEntity.ok(adminService.getAllRolesPermission());
    }


    public ResponseEntity<String> updateUserDataByAdmin(UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(adminService.updateUserByAdmin(userUpdateRequest));
    }


}
