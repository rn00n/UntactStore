package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.menu.form.SetmenuForm;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.order.Cart;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
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
        Store store = storeRepository.findStoreWithMenusByPath(path);
        model.addAttribute(store);
        
        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);//TODO n+1성능
        model.addAttribute("tables", tables);

        model.addAttribute("orders", tables.getOrderList());

        List<Setmenu> setmenuList = setmenuRepository.findAllByStore(store);
        model.addAttribute("setmenuList", setmenuList);

        model.addAttribute("menuList", store.getMenuList());

        model.addAttribute("cart", cart);
        return "tables/view";
    }
    /*테이블 장바구니 - 세트메뉴 추가*/
    @GetMapping("/{tables-path}/cart/add-setmenu")
    public String addSetmenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Setmenu> setmenu = setmenuRepository.findById(Long.valueOf(id));
        cart.getOrders().getSetMenuList().add(setmenu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }
    /*테이블 장바구니 - 메뉴 추가*/
    @GetMapping("/{tables-path}/cart/add-menu")
    public String addMenuOfCart(@CurrentAccount Account account, @RequestParam("id") String id, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model) {
        Optional<Menu> menu = menuRepository.findById(Long.valueOf(id));
        cart.getOrders().getMenuList().add(menu.orElseThrow());

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }

    @GetMapping("/{tables-path}/add-orders")
    public String addOrders(@CurrentAccount Account account, @PathVariable("path") String path, Cart cart, @PathVariable("tables-path") String tablesPath, Model model, SessionStatus sessionStatus) {
        Store store = storeRepository.findStoreWithMenusByPath(path);

        Tables tables = tablesRepository.findByStoreAndTablesPath(store, tablesPath);
        tablesService.addOrders(store, tables, cart);

        sessionStatus.setComplete();
        cart = new Cart();
        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/tables/"+URLEncoder.encode(tablesPath, StandardCharsets.UTF_8);
    }
}
