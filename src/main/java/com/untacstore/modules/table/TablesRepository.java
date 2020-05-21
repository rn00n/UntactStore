package com.untacstore.modules.table;

import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface TablesRepository extends JpaRepository<Tables, Long> {
    Tables findByStoreAndTablesPath(Store store, String tablesPath);
}
