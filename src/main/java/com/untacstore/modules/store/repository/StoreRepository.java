package com.untacstore.modules.store.repository;

import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StoreRepository extends JpaRepository<Store, Long> {
}
