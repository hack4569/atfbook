package com.shsb.atfbook.application.category;

import com.shsb.atfbook.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByDepth1In(List<String> depth1s);
}
