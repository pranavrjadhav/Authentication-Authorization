package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.CarDto;
import com.zenton.auth.Authorization.entity.Car;
import com.zenton.auth.Authorization.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public Car createCar(CarDto dto) {

        Car car = Car.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .build();

        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
