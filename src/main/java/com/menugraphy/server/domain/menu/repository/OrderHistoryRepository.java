package com.menugraphy.server.domain.menu.repository;

import com.menugraphy.server.domain.menu.model.entity.OrderHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findAllByMemberId(Long memberId);
}
