package com.example.tree_category_manager.service.impl;

import com.example.tree_category_manager.entity.CategoryTree;
import com.example.tree_category_manager.repository.CategoryTreeRepository;
import com.example.tree_category_manager.service.CategoryTreeService;
import org.springframework.stereotype.Service;

import java.util.List;


public class CategoryTreeServiceImpl implements CategoryTreeService {
    private final CategoryTreeRepository categoryTreeRepository;

    public CategoryTreeServiceImpl(CategoryTreeRepository categoryTreeRepository) {
        this.categoryTreeRepository = categoryTreeRepository;
    }

    @Override
    public CategoryTree getCategoryTree(String name) {
        return categoryTreeRepository.findByName(name);
    }

    @Override
    public CategoryTree getRootCategories() {
        return categoryTreeRepository.findByParentIsNull();
    }

    @Override
    public List<CategoryTree> showAllTrees() {
        return categoryTreeRepository.findAll();
    }

    @Override
    public CategoryTree createCategory(String parentClassName, String mainName) {
        CategoryTree byParentName = categoryTreeRepository.findByName(parentClassName);
        CategoryTree categoryTree = new CategoryTree(mainName, byParentName);
        return categoryTreeRepository.save(categoryTree);
    }

    @Override
    public CategoryTree updateCategory(String oldName, String newName) {
        CategoryTree categoryTree = categoryTreeRepository.findByName(oldName);
        categoryTree.setName(newName);
        return categoryTree;
    }

    @Override
    public CategoryTree deleteCategory(String categoryName) {
        CategoryTree categoryTree = categoryTreeRepository.findByName(categoryName);
        Long id = categoryTree.getId();
        categoryTreeRepository.deleteById(id);
        return categoryTree;
    }

    @Override
    public CategoryTree addSubClasses(Long parentId, String name) {
        CategoryTree parent = categoryTreeRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));
        CategoryTree child = new CategoryTree(name, parent);
        return categoryTreeRepository.save(child);
    }
}
