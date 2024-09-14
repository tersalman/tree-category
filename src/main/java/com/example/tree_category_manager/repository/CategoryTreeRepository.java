package com.example.tree_category_manager.repository;

import com.example.tree_category_manager.entity.CategoryTree;
import com.example.tree_category_manager.service.CategoryTreeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryTreeRepository extends JpaRepository<CategoryTree, Long> {
    CategoryTree findByParent(CategoryTree parent);
    CategoryTree findByName(String name);

    CategoryTree findByParentIsNull();

}
