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

    @OneToMany
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany
    private List<Setmenu> setMenuList = new ArrayList<>();

    private LocalDateTime orderAt;
}
