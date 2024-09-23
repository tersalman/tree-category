package com.example.tree_category_manager.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class CategoryTree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private CategoryTree parent;
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CategoryTree> subClasses = new ArrayList<>();

    public CategoryTree() {
    }

    public CategoryTree(String name,CategoryTree parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder subClassesStr = new StringBuilder();
        for (CategoryTree subClass : subClasses) {
            subClassesStr.append(subClass.getName()).append(", ");
        }

        return "CategoryTree{" +
                "mainName='" + name + '\'' +
                ", fatherName='" + (parent != null ? parent.getName() : "null") + '\'' +
                ", subCategories='" + subClassesStr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryTree that = (CategoryTree) o;
        return Objects.equals(name, that.name) && Objects.equals(parent, that.parent) && Objects.equals(subClasses, that.subClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent, subClasses);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryTree getParent() {
        return parent;
    }

    public void setParent(CategoryTree parent) {
        this.parent = parent;
    }

    public List<CategoryTree> getSubClasses() {
        return subClasses;
    }

    public void setSubClasses(List<CategoryTree> subClasses) {
        this.subClasses = subClasses;
    }

    public Long getId() {
        return id;
    }

    public void addChildCategory(CategoryTree child) {
        child.setParent(this);
        this.subClasses.add(child);
    }
}
