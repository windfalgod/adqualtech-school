package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.PrepareExam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepareExamRepository extends JpaRepository<PrepareExam, Long> {

    @Query(value = "select p from PrepareExam p order by p.createdAt")
    List<PrepareExam> findAllOrderByCreatedAtDesc();

    @Query(value = "select p from PrepareExam p inner join Scope s on p.scope.id = s.id where s.title like :gradeName")
    List<PrepareExam> findExamsByScope_IdOrderByCreatedAtDesc(@Param("gradeName")String gradeName);

    List<PrepareExam> findAllBySubject_NameOrderByCreatedAtDesc(String subjectName);

    List<PrepareExam> findAllByCreator_Id(Long creator_id);

    List<PrepareExam> findAllByScope_TitleOrderByCreatedAtDesc(String title);

    @Query(value = "select p from PrepareExam p inner join Account a on a.id = p.creator.id" +
            " where lower(concat(p.creator.lastName, ' ', p.creator.firstName)) like lower(concat('%', :search, '%'))" +
            " or lower(p.title) like lower(:search)")
    List<PrepareExam> searchByTitleOrCreatorNameOrderByCreatedAtDesc(@Param("search")String search);
}
