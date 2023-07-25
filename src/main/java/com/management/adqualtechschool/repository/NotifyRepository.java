package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Notify;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like :className")
    List<Notify> findNotifiesByClassName(@Param("className")String className);

    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like :gradeName")
    List<Notify> findNotifiesByGradeName(@Param("gradeName")String gradeName);

    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like 'Toàn trường'")
    List<Notify> findNotifiesBySchoolWide();
}