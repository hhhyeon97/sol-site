package com.sol.shop.member;

import com.sol.shop.sales.Sales;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String displayName;
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Sales> sales = new ArrayList<>();

    private String role;

}
