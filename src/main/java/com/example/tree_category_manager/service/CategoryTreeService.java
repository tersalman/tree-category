package com.example.tree_category_manager.service;

import com.example.tree_category_manager.entity.CategoryTree;

import java.util.List;

public interface CategoryTreeService {

    List<CategoryTree> showAllTrees();
    CategoryTree getCategoryTree(String name);

    List<CategoryTree> getRootCategories();
    CategoryTree createCategory(String mainName);
    CategoryTree updateCategory(String oldName, String newName);
    CategoryTree deleteCategory(String categoryName);
    CategoryTree addSubCategory(String parent, String name);
    List<CategoryTree> getAllSubcategories(String categoryName);

    CategoryTree getCategoryParent(String name);

}
