package com.management.adqualtechschool.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class PostEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Account creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scope_id", referencedColumnName = "id")
    private Scope scope;
}
