package com.sol.shop.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@Table(indexes = @Index(columnList = "title", name = "작명"))
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    private String title;

    private Integer price;
    private String imageUrl;
    private String descContent;
    private Long userId;

}
