package com.zenton.auth.Authorization.repository;

import com.zenton.auth.Authorization.entity.Car;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByUserId (Long id);  //Property Traversal Query Method
}