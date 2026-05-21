package com.zenton.auth.Authorization.controller;

import com.zenton.auth.Authorization.dtos.CarDto;
import com.zenton.auth.Authorization.entity.Car;
import com.zenton.auth.Authorization.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<Car> createCar(
            @RequestBody CarDto dto
    ) {

        return ResponseEntity.ok(
                carService.createCar(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {

        return ResponseEntity.ok(
                carService.getAllCars()
        );
    }
}
