package com.example.application.repo;

import com.example.application.entity.Menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepo extends JpaRepository<Menu, Integer> {
    Menu findByMenuName(String menuName);
}
