package com.example.application.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@NoArgsConstructor
public class User {
    @Id
    @NonNull
    private String username;
    @NonNull
    private String password;
}
