package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select e from Event e join Scope s on e.scope.id = s.id where s.title like :className")
    List<Event> findEventsByClassName(@Param("className")String className);

    @Query(value = "select e from Event e join Scope s on e.scope.id = s.id where s.title like :gradeName")
    List<Event> findEventsByGradeName(@Param("gradeName")String gradeName);

    @Query(value = "select e from Event e join Scope s on e.scope.id = s.id where s.title like 'Toàn trường'")
    List<Event> findEventsBySchoolWide();
}
