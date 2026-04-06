package com.ecommerce.project.repositories;

import com.ecommerce.project.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    CategoryModel findByCategoryName(String categoryName);
}