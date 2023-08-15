package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    @Query(value = "select a from Account a where a.position like 'Giáo viên'" +
            " order by a.firstName, a.lastName asc")
    List<Account> findAllTeacherByPosition(String position);

    @Query(value = "select MAX(a.id) from Account a")
    Long findMaxId();

    @Query(value = "select a from Account a where a.position like 'Giáo viên' and" +
            "(lower(concat(a.lastName, ' ', a.firstName)) like lower(trim(concat('%', :search, '%')))" +
            " or lower(a.level) like trim(concat('%', :search, '%'))" +
            " or lower(a.rank) like trim(concat('%', :search, '%'))" +
            " or lower(a.position) like trim(concat('%', :search, '%'))" +
            " or lower(a.phone) like trim(concat('%', :search, '%'))" +
            " or lower(a.email) like trim(concat('%', :search, '%')))" +
            " order by a.firstName, a.lastName ASC ")
    List<Account> searchTeachers(@Param("search")String search);

    @Query(value = "select a from Account a where a.username like 'pu%'")
    List<Account> findAllPupil();

    List<Account> findAllPupilByClassroomName(String className);

    @Query(value = "select a from Account a where a.classroom.name like concat(:gradeName, '%')")
    List<Account> findAllPupilByGradeName(@Param("gradeName") String gradeName);

    @Query(value = "select a from Account a where lower(concat(a.lastName, ' ', a.firstName))" +
            " like trim(concat('%', :search, '%'))" +
            " and a.username like 'pu%' " +
            " order by a.firstName, a.lastName ASC ")
    List<Account> searchPupil(@Param("search") String search);
}
