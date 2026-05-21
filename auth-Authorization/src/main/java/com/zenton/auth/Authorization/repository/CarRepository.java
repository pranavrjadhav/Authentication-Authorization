package com.zenton.auth.Authorization.repository;

import com.zenton.auth.Authorization.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}