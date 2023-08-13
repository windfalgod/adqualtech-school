package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.PrepareExam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepareExamRepository extends JpaRepository<PrepareExam, Long> {

    @Query(value = "select p from PrepareExam p order by p.updatedAt desc")
    List<PrepareExam> findAllOrderByUpdatedAtDesc();

    @Query(value = "select p from PrepareExam p inner join Scope s on p.scope.id = s.id" +
            " where s.title like :gradeName order by p.updatedAt desc ")
    List<PrepareExam> findAllByGradeName(@Param("gradeName")String gradeName);

    @Query(value = "select p from PrepareExam p inner join Account a on a.id = p.creator.id" +
            " where lower(concat(p.creator.lastName, ' ', p.creator.firstName)) " +
            " like trim(lower(concat('%', :search, '%')))" +
            " or lower(p.title) like trim(lower(concat('%', :search, '%')))" +
            " order by p.updatedAt desc ")
    List<PrepareExam> searchByTitleOrCreatorNameOrderByUpdatedAtDesc(@Param("search")String search);
}
