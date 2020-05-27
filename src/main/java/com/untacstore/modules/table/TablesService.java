package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.order.*;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TablesService {
    private final TablesRepository tablesRepository;
    private final StoreRepository storeRepository;
    private final OrdersRepository ordersRepository;
    private final SetmenuRepository setmenuRepository;
    private final MenuRepository menuRepository;
    private final EventRepository eventRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

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

        tables.setPay(tables.getPay()+orders.getOrdersAmount());
    }

    public void removeOrders(Store store, Tables tables, Orders orders) {
        ordersRepository.delete(orders);
        tables.setPay(tables.getPay()-orders.getOrdersAmount());
        tables.getOrderList().remove(orders);
    }

    public void sitDownRequest(Tables tables, Account account) {
        Event event = new Event();
        event.setAccount(account);
        event.setTurn(tables.getEventList().size()+1);
        event.setPersonnel(4);
        event.setEventAt(LocalDateTime.now());
        tables.addEvent(event);
        eventRepository.save(event);
    }

    public void sitUpRequest(Tables tables, Event event) {
        tables.removeEvent(event);
        eventRepository.delete(event);
    }

    public void sitAccept(Tables tables, Event event) {
        tables.setAccount(event.getAccount());
        tables.setStartedAt(LocalDateTime.now());
        tables.setOrderList(new ArrayList<>());
        tables.setEventList(new ArrayList<>());
        tables.setPay(0);
        event.setAccept(true);

        //나머지 대기자들 요청취소 처리
//        eventRepository.deleteAllByTables(tables);
    }

    public void sitAcceptCancel(Tables tables, Event event) {
        tables.setStartedAt(null);
        tables.setAccount(null);
        event.setAccept(false);
    }

    public RequestOrder addRequestOrder(RequestOrder requestOrder) {
        return requestOrderRepository.save(requestOrder);
    }

    public void removeRequestOrder(RequestOrder requestOrder) {
        requestOrderRepository.delete(requestOrder);
    }

    public void requestPayment(Tables tables) {
        tables.setRequestPayment(true);
    }

    public void cancelRequestPayment(Tables tables) {
        tables.setRequestPayment(false);
    }

    public Tables completePaymentMiddle(Tables tables) {
        Payment payment = modelMapper.map(tables, Payment.class);
        payment.setTables(tables);
        payment.setPaymentAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        payment.getOrderList().stream().forEach(o->o.setCompletePayment(true));

        ordersRepository.saveAll(payment.getOrderList());

        tables.setPay(0);
        tables.setOrderList(new ArrayList<>());
        tables.setRequestPayment(false);
        return tables;
    }

    public void completePayment(Tables tables) {
        tables = completePaymentMiddle(tables);
        eventRepository.deleteAllByTables(tables);

        tables.setAccount(null);
        tables.setPay(null);
        tables.setStartedAt(null);
        tables.setEventList(null);
        tables.setOrderList(null);
        tables.setRequestPayment(false);
    }
}
