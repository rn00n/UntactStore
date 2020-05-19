package com.untacstore.modules.menu.repository;

import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetmenuRepository extends JpaRepository<Setmenu, Long> {

    List<Setmenu> findAllByStore(Store store);

}
