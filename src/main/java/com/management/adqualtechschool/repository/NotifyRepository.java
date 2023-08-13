package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Notify;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like :className")
    List<Notify> findNotifiesByClassNameOrderByCreatedAtDesc(@Param("className")String className);

    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like :gradeName")
    List<Notify> findNotifiesByGradeNameOrderByCreatedAtDesc(@Param("gradeName")String gradeName);

    @Query(value = "select n from Notify n join Scope s on n.scope.id = s.id where s.title like 'Toàn trường'")
    List<Notify> findNotifiesBySchoolWideOrderByCreatedAtDesc();

    @Query(value = "select distinct (n.creator) from Notify n group by n.creator order by n.creator.firstName, n.creator.lastName asc ")
    List<Account> findAllNotifyCreator();
}
