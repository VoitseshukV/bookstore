package com.bookstore.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET is_deleted = TRUE WHERE id=?")
@Where(clause = "is_deleted = FALSE")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String name;
    private String description;
    @Column(name = "is_deleted")
    @NotNull
    private boolean isDeleted = false;

    public Category(Long id) {
        this.id = id;
    }
}
