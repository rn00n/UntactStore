package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByTablesAndAccount(Tables tables, Account account);

    void deleteAllByTables(Tables tables);

}
