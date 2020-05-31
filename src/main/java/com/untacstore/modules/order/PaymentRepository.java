package com.untacstore.modules.order;

import com.untacstore.modules.store.Store;
import com.untacstore.modules.table.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByTables(Tables tables);

    List<Payment> findAllByStore(Store store);

    List<Payment> findAllByStoreAndTables(Store store, Tables tables);

    List<Payment> findAllByStoreOrderByPaymentAtDesc(Store store);

    List<Payment> findAllByStoreAndTablesOrderByPaymentAtDesc(Store store, Tables tables);
//    select * form payment where store_id='store' and tables_id='tables' and startedAt > '2020-05-27' and paymentAt < '2020-05-28';
}
