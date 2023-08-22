package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.StudyRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {

    @Query(value = "select s from StudyRoom s order by s.name asc")
    List<StudyRoom> findAllStudyRoom();
}
