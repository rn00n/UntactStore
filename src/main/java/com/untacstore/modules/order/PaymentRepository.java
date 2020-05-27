package com.untacstore.modules.order;

import com.untacstore.modules.table.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByTables(Tables tables);
}
