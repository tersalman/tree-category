package com.example.tree_category_manager.repository;

import com.example.tree_category_manager.entity.CategoryTree;
import com.example.tree_category_manager.service.CategoryTreeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryTreeRepository extends JpaRepository<CategoryTree, Long> {
    /**
     * method for finding CategoryTree object by name
     * @param name
     * @return Found CategoryTree object
     */
    CategoryTree findByName(String name);

    /**
     * method which find a CategoryTree object without parent
     * @return List of CategoryTree
     */
    List<CategoryTree> findByParentIsNull();

    CategoryTree findParentByName(String name);

}
