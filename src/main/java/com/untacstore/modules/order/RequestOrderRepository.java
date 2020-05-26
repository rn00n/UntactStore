package com.untacstore.modules.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long> {
}
