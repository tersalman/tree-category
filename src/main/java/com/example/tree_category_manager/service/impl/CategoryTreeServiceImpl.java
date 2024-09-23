package com.example.tree_category_manager.service.impl;

import com.example.tree_category_manager.entity.CategoryTree;
import com.example.tree_category_manager.repository.CategoryTreeRepository;
import com.example.tree_category_manager.service.CategoryTreeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryTreeServiceImpl implements CategoryTreeService {
    private final CategoryTreeRepository categoryTreeRepository;

    public CategoryTreeServiceImpl(CategoryTreeRepository categoryTreeRepository) {
        this.categoryTreeRepository = categoryTreeRepository;
    }

    /**
     * method for get CategoryTree object by name
     * @param name
     * @return CategoryTree object
     */
    @Override
    public CategoryTree getCategoryTree(String name) {
        CategoryTree byName = categoryTreeRepository.findByName(name);
        return byName;
    }

    /**
     * method which return List of CategoryTree subclasses without main name and parents
     * @return List of CategoryTree
     */
    @Override
    public List<CategoryTree> getRootCategories() {
        return categoryTreeRepository.findByParentIsNull();
    }

    /**
     * method which using repository to get all category tree
     * @return List of CategoryTree
     */
    @Override
    public List<CategoryTree> showAllTrees() {
        return categoryTreeRepository.findAll();
    }

    /**
     * method for creating category without parent class
     * @param mainName
     * @return CategoryTree object without parent and children
     */
    @Override
    public CategoryTree createCategory(String mainName) {
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setName(mainName);
        return categoryTreeRepository.save(categoryTree);
    }

    /**
     * method using param oldName to find CategoryTree object and using a setter to update his name
     * @param oldName
     * @param newName
     * @return CategoryTree object with new name
     */
    @Override
    public CategoryTree updateCategory(String oldName, String newName) {
        CategoryTree categoryTree = categoryTreeRepository.findByName(oldName);
        categoryTree.setName(newName);
        return categoryTreeRepository.save(categoryTree);
    }

    /**
     * method using param categoryName to delete wanted object
     * @param categoryName
     * @return deleted CategoryTree object
     */
    @Override
    public CategoryTree deleteCategory(String categoryName) {
        CategoryTree categoryTree = categoryTreeRepository.findByName(categoryName);
        Long id = categoryTree.getId();
        categoryTreeRepository.deleteById(id);
        return categoryTree;
    }

    /**
     * method which find a CategoryTree object By param parent,
     * create a new object with param name and set this object to parent object as children
     * @param parent
     * @param name
     * @return CategoryTree child
     */
    @Override
    public CategoryTree addSubCategory(String parent, String name) {
        CategoryTree parentCategory = categoryTreeRepository.findByName(parent);
        CategoryTree child = new CategoryTree(name, parentCategory);
        parentCategory.addChildCategory(child);
        return categoryTreeRepository.save(child);
    }

    /**
     * Method for getting List&lt;CategoryTree&gt; by name
     * @param categoryName
     * @return List&lt;CategoryTree&gt; childClasses
     */
    @Override
    public List<CategoryTree> getAllSubcategories(String categoryName) {

            CategoryTree categoryTree = categoryTreeRepository.findByName(categoryName);
            List<CategoryTree> subClasses = categoryTree.getSubClasses();

            return subClasses;
    }

    @Override
    public CategoryTree getCategoryParent(String name) {
        return categoryTreeRepository.findParentByName(name);
    }
}
