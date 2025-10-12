package com.shsb.atfbook.application.aladin;

import com.shsb.atfbook.domain.aladin.AladinBook;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AladinBookRepository extends JpaRepository<AladinBook, Integer> {

    List<AladinBook> findAll(Sort sort);
}
