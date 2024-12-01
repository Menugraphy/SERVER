package com.menugraphy.server.domain.food.model.entity;

import com.menugraphy.server.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food")
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image")
    private String image;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "food_type_list", columnDefinition = "jsonb")
    private List<Long> foodTypeList;

//    @Column(name = "likes")
//    private int likes;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "similar_food_list", columnDefinition = "jsonb")
    private List<Long> similarFoodList;

    @Builder
    private Food(
            final String image,
            final List<Long> foodTypeList,
//            final int likes,
            final String name,
            final String description,
            final List<Long> similarFoodList
    ) {
        this.image = image;
        this.foodTypeList = foodTypeList;
//        this.likes = likes;
        this.name = name;
        this.description = description;
        this.similarFoodList = similarFoodList;
    }
}
