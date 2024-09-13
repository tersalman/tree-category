package com.example.tree_category_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.LinkedList;
import java.util.Objects;

@Entity
public class CategoryTree {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mainName;
    private String fatherName;
    private String sonName;

    public CategoryTree() {
    }

    public CategoryTree(String mainName, String fatherName, String sonName) {
        this.mainName = mainName;
        this.fatherName = fatherName;
        this.sonName = sonName;
    }

    @Override
    public String toString() {
        return "CategoryTree{" +
                "mainName='" + mainName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", sonName='" + sonName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryTree that = (CategoryTree) o;
        return Objects.equals(mainName, that.mainName) && Objects.equals(fatherName, that.fatherName) && Objects.equals(sonName, that.sonName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainName, fatherName, sonName);
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getSonName() {
        return sonName;
    }

    public void setSonName(String sonName) {
        this.sonName = sonName;
    }

    public Long getId() {
        return id;
    }
}
