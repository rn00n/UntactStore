package com.untacstore.modules.menu.repository;

import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SetmenuRepository extends JpaRepository<Setmenu, Long> {
    @EntityGraph(attributePaths = {"menuList"})
    List<Setmenu> findSetmenuWithMenuListByStore(Store store);
}
