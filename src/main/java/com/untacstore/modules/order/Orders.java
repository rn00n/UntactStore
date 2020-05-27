package com.untacstore.modules.order;

import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.table.Tables;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Orders {
    @Id @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private Tables tables;

    private Integer ordersAmount = 0;

    @ManyToMany
    private List<Menu> menuList = new ArrayList<>();

    @ManyToMany
    private List<Setmenu> setMenuList = new ArrayList<>();

    @ManyToMany
    private List<RequestOrder> requestOrderList = new ArrayList<>();

    private LocalDateTime orderAt;

    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType = null;

    public String getStatus() {
        if (orderStatusType.equals(OrderStatusType.NEW_ORDER)) {
            return "확인전";
        } else if (orderStatusType.equals(OrderStatusType.BEFORE_COMPLETE)) {
            return "진행중";
        } else if (orderStatusType.equals(OrderStatusType.AFTER_COMPLETE)) {
            return "완료";
        } else if (orderStatusType.equals(OrderStatusType.COMPLETE_PAYMENT)) {
            return "결제완료";
        }
        return "";
    }
}
