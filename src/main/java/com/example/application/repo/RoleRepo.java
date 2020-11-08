package com.example.application.repo;

import com.example.application.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
