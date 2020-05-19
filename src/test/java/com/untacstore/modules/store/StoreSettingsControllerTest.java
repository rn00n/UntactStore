package com.untacstore.modules.store;

import com.untacstore.modules.account.WithAccount;
import com.untacstore.modules.keyword.KeywordRepository;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.table.TablesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StoreSettingsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    TablesRepository tablesRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    SetmenuRepository setmenuRepository;

    final String path = "/store/{path}/settings";


}