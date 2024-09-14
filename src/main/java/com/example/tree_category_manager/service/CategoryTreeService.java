package com.example.tree_category_manager.service;

import com.example.tree_category_manager.entity.CategoryTree;

import java.util.List;

public interface CategoryTreeService {

    List<CategoryTree> showAllTrees();
    CategoryTree getCategoryTree(String name);

    CategoryTree getRootCategories();
    CategoryTree createCategory(String parentClassName, String mainName);
    CategoryTree updateCategory(String oldName, String newName);
    CategoryTree deleteCategory(String categoryName);
    CategoryTree addSubClasses(Long parentId, String name);

}
