package com.menugraphy.server.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_like",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_member_id_food_id",
                columnNames = {"member_id", "food_id"}
        )
)
public class MemberLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private long memberId;

    @Column(name = "food_id", nullable = false)
    private long foodId;

    @Builder
    private MemberLike(
            final long memberId,
            final long foodId
    ) {
        this.memberId = memberId;
        this.foodId = foodId;
    }
}
