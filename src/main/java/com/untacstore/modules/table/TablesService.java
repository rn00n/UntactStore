package com.untacstore.modules.table;

import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.order.Cart;
import com.untacstore.modules.order.Orders;
import com.untacstore.modules.order.OrdersRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TablesService {
    private final TablesRepository tablesRepository;
    private final StoreRepository storeRepository;
    private final OrdersRepository ordersRepository;

    public void addOrders(Store store, Tables tables, Cart cart) {
        Orders orders = cart.getOrders();
        orders.setOrderAt(LocalDateTime.now());
        orders.setTables(tables);
        orders.setTitle("");

        int totalPrice = 0;
        List<Integer> menuPrice = orders.getMenuList().stream().map(Menu::getPrice).collect(Collectors.toList());
        for(int i:menuPrice) {
            totalPrice += i;
        }
        List<Integer> SetmenuTotalPrice = orders.getSetMenuList().stream().map(Setmenu::getTotalPrice).collect(Collectors.toList());
        for (int i:SetmenuTotalPrice) {
            totalPrice += i;
        }

        orders.setOrdersAmount(totalPrice);

        tables.getOrderList().add(orders);
        ordersRepository.save(orders);

        tables.setAmount(tables.getAmount()+orders.getOrdersAmount());
    }
}
