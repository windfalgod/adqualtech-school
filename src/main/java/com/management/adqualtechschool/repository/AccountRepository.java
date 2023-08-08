package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    @Query(value = "select a from Account a where a.username like 'te%' or a.username like 'ma%'")
    List<Account> findAllTeacherManager();
}
