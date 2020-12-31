package com.example.application.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleId;

    @NonNull
    private String roleName;

    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_menus", joinColumns = @JoinColumn(), inverseJoinColumns = @JoinColumn())
    private List<Menu> menus;
}
