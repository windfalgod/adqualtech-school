package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select e from Event e inner join Scope s on e.scope.id = s.id where s.title like :className")
    List<Event> findEventsByClassNameOrderByCreatedAtDesc(@Param("className")String className);

    @Query(value = "select e from Event e inner join Scope s on e.scope.id = s.id where s.title like :gradeName")
    List<Event> findEventsByGradeNameOrderByCreatedAtDesc(@Param("gradeName")String gradeName);

    @Query(value = "select e from Event e inner join Scope s on e.scope.id = s.id where s.title like 'Toàn trường'")
    List<Event> findEventsBySchoolWideOrderByCreatedAtDesc();

    @Query(value = "select distinct (e.creator) from Event e group by e.creator order by e.creator.firstName, e.creator.lastName asc ")
    List<Account> findAllEventCreator();
}
