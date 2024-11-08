package com.menugraphy.server.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food_avoidance")
public class FoodAvoidance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private long memberId;

    @Column(name = "category_id", nullable = false)
    private long categoryId;

    @Column(name = "type_id", nullable = false)
    private long typeId;

    @Builder
    private FoodAvoidance(
            final long memberId,
            final long categoryId,
            final long typeId
    ) {
        this.memberId = memberId;
        this.categoryId = categoryId;
        this.typeId = typeId;
    }
}
