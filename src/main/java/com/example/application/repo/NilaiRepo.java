package com.example.application.repo;

import java.util.List;

import com.example.application.entity.Nilai;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NilaiRepo extends JpaRepository<Nilai, Long> {

}
