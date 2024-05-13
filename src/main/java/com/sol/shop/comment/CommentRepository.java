package com.sol.shop.comment;

import com.sol.shop.sales.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByParentId(Long parentId);

    List<Comment> findByUsername(String username);

    Page<Comment> findAll(Pageable pageable);

}
