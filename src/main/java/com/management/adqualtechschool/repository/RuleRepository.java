package com.management.adqualtechschool.repository;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Rule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    @Query(value = "select e from Rule e inner join Scope s on e.scope.id = s.id where s.title like lower(:className) ")
    List<Rule> findRulesByClassNameOrderByCreatedAtDesc(@Param("className")String className);

    @Query(value = "select e from Rule e inner join Scope s on e.scope.id = s.id where s.title like lower(:gradeName)")
    List<Rule> findRulesByGradeNameOrderByCreatedAtDesc(@Param("gradeName")String gradeName);

    @Query(value = "select e from Rule e inner join Scope s on e.scope.id = s.id where s.title like 'Toàn trường'")
    List<Rule> findRulesBySchoolWideOrderByCreatedAtDesc();

    @Query(value = "select distinct (r.creator) from Rule r group by r.creator" +
            " order by r.creator.firstName, r.creator.lastName asc ")
    List<Account> findAllRuleCreator();
}
