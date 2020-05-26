package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.menu.form.SetmenuForm;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.order.*;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/store/{path}/tables")
@RequiredArgsConstructor
@SessionAttributes("cart")
public class TablesController {
    private final TablesRepository tablesRepository;
    private final StoreRepository storeRepository;
    private final TablesService tablesService;
    private final SetmenuRepository setmenuRepository;
    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final EventRepository eventRepository;
    private final AccountRepository accountRepository;
    private final RequestOrderRepository requestOrderRepository;

    /*테이블 리스트 - 뷰*/
    @GetMapping("/list")
    public String tablesListView(@CurrentAccount Account account, @PathVariable("path") String path, Model model) {
        model.addAttribute(account);
        Store store = storeRepository.findStoreByPath(path);
        model.addAttribute(store);

        model.addAttribute("tablesList", store.getTableList());
        return "tables/list";
    }
    
    /*테이블 - 뷰*/
    @GetMapping("/{tables-path}")
    public String tablesView(@CurrentAccount Account account, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        System.out.println("log1");
        Store store = storeRepository.findStoreWithMenusByPath(path);
        model.addAttribute(store);
        System.out.println("log2");
        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);//TODO n+1성능
        model.addAttribute("tables", tables);
        System.out.println("log3");
        model.addAttribute("orders", tables.getOrderList());
        System.out.println("log4");
        List<Setmenu> setmenuList = setmenuRepository.findAllByStore(store);
//        List<Setmenu> setmenuList = store.getSetmenuList();
        model.addAttribute("setmenuList", setmenuList);
        System.out.println("log5");
        model.addAttribute("menuList", store.getMenuList());
        System.out.println("log6");
        model.addAttribute("cart", cart);
        System.out.println("log7");

        String qrCode = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=http://localhost:8080/store/"+path+"/tables/"+tablesPath;
        model.addAttribute("qrcode", qrCode);
        return "tables/view";
    }

    /*테이블 장바구니 - 세트메뉴 추가*/
    @GetMapping("/{tables-path}/cart/add-setmenu")
    public String addSetmenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Setmenu> setmenu = setmenuRepository.findById(Long.valueOf(id));
        cart.getOrders().getSetMenuList().add(setmenu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 장바구니 - 세트메뉴 제거*/
    @GetMapping("/{tables-path}/cart/remove-setmenu")
    public String removeSetmenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Setmenu> setmenu = setmenuRepository.findById(Long.valueOf(id));
        cart.getOrders().getSetMenuList().remove(setmenu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 장바구니 - 메뉴 추가*/
    @GetMapping("/{tables-path}/cart/add-menu")
    public String addMenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Menu> menu = menuRepository.findById(Long.valueOf(id));
        cart.getOrders().getMenuList().add(menu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 장바구니 - 메뉴 제거*/
    @GetMapping("/{tables-path}/cart/remove-menu")
    public String removeMenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Menu> menu = menuRepository.findById(Long.valueOf(id));
        cart.getOrders().getMenuList().remove(menu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 장바구니 - 요청사항 추가*/
    @PostMapping("/{tables-path}/cart/add-request")
    public String addRequestOrder(@CurrentAccount Account account, @RequestParam String request, Cart cart, @PathVariable("path") String path, @PathVariable("tables-path") String tablesPath, Model model) {
        RequestOrder requestOrder = tablesService.addRequestOrder(RequestOrder.builder().content(request).build());

        cart.getOrders().getRequestOrderList().add(requestOrder);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 장바구니 - 요청사항 삭제*/
    @GetMapping("/{tables-path}/cart/remove-request")
    public String removeRequestOrder(@CurrentAccount Account account, @RequestParam String id, Cart cart, @PathVariable("path") String path, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<RequestOrder> byId = requestOrderRepository.findById(Long.valueOf(id));
        RequestOrder requestOrder = byId.orElseThrow();
        cart.getOrders().getRequestOrderList().remove(requestOrder);

        tablesService.removeRequestOrder(requestOrder);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 주문 - 추가*/
    @GetMapping("/{tables-path}/add-orders")
    public String addOrders(@CurrentAccount Account account, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model, SessionStatus sessionStatus) {
        Store store = storeRepository.findStoreWithMenusByPath(path);

        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);
        tablesService.addOrders(store, tables, cart);

        sessionStatus.setComplete();
        cart = new Cart();
        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 주문 - 제거*/
    @GetMapping("/{tables-path}/remove-orders")
    public String removeOrders(@CurrentAccount Account account, @PathVariable("path") String path, @RequestParam("id") String id, @PathVariable("tables-path") String tablesPath, Model model) {
        Store store = storeRepository.findStoreWithMenusByPath(path);

        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);

        Optional<Orders> orders = ordersRepository.findById(Long.valueOf(id));
        tablesService.removeOrders(store, tables, orders.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 착석 - 요청*/
    @PostMapping("/{tables-path}/sit-down")
    public String sitDownRequest(@CurrentAccount Account account, @PathVariable("path") String path, @PathVariable("tables-path") String tablesPath, Model model) {
        Store store = storeRepository.findStoreByPath(path);
        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);
        tablesService.sitDownRequest(tables, account);
        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 착석 - 요청 취소*/
    @PostMapping("/{tables-path}/sit-up")
    public String sitUpRequest(@CurrentAccount Account account, @PathVariable("path") String path, @PathVariable("tables-path") String tablesPath, Model model) {
        Store store = storeRepository.findStoreByPath(path);
        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);

        Event event = eventRepository.findByTablesAndAccount(tables, account);
        tablesService.sitUpRequest(tables, event);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    /*테이블 착석 - 수락*/
    @GetMapping("/{tables-path}/sit-accept")
    public String sitAccept(@CurrentAccount Account account, @RequestParam String id, @PathVariable("path") String path, @PathVariable("tables-path") String tablesPath, Model model) {
        Store store = storeRepository.findStoreByPath(path);
        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);

        Optional<Account> byId = accountRepository.findById(Long.valueOf(id));
        Event event = eventRepository.findByTablesAndAccount(tables, byId.orElseThrow());
        tablesService.sitAccept(tables, event);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

}
