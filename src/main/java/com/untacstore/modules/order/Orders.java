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

    @ManyToOne
    private Tables tables;

    @OneToMany
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany
    private List<Setmenu> setMenuList = new ArrayList<>();

    private LocalDateTime orderAt;
}
