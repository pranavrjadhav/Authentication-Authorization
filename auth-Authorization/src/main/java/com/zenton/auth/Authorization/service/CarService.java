package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.config.SecurityConfigUtil;
import com.zenton.auth.Authorization.dtos.Cachedtos.CachedUser;
import com.zenton.auth.Authorization.dtos.Cachedtos.CarDto;
import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
import com.zenton.auth.Authorization.entity.Car;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.CarRepository;
import com.zenton.auth.Authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final SecurityConfigUtil securityConfigUtil;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Car createCar(CarDto dto) {
        AuthenticatedUser authenticatedUser = securityConfigUtil.getCurrentUser();
       Car car = Car.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .userId(authenticatedUser.getId())
                .build();

        return carRepository.save(car);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Car> getAllCars() {
        AuthenticatedUser authenticatedUser = securityConfigUtil.getCurrentUser();
        return carRepository.findByUserId(authenticatedUser.getId());
    }
}
