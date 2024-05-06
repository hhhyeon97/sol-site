package com.sol.shop.sales;

import com.sol.shop.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {


    @Query(value = "select s from Sales s join fetch s.member")
    List<Sales> customFindAll();

    List<Sales> findByMemberId(Long memberId);
}
