package com.management.adqualtechschool.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role extends BaseEntity{

    @Column(name = "name", length = 12, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;
}
