package com.untacstore.modules.order;

import com.untacstore.modules.table.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    void deleteAllByTables(Tables tables);
    List<Orders> findAllByTablesAndCompletePayment(Tables tables, boolean completePayment);
}
