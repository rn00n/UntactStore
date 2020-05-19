package com.untacstore.modules.menu.repository;

import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);
}
