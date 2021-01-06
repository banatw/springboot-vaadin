package com.example.application.repo;

import java.util.List;

import com.example.application.entity.Menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepo extends JpaRepository<Menu, Integer> {
    public Menu findByMenuName(String menuName);

    public Menu findByPath(String path);

    public List<Menu> findByShowInMenuBar(Boolean showInMenuBar);
}
