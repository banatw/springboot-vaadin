package com.example.application.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Matakuliah {
    @Id
    @NonNull
    private Integer idMatakuliah;
    @NonNull
    private String namaMatakuliah;
}
