package com.untacstore.modules.menu.service;

import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.form.MenuForm;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public void addMenu(Store store, Menu newMenu) {
        newMenu.setStore(store);
        menuRepository.save(newMenu);
    }

    public void removeMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
