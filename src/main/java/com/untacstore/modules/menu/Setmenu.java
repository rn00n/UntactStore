package com.untacstore.modules.menu;

import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Setmenu {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    @OneToMany
    private List<Menu> menuList = new ArrayList<>();

    private Integer price;
}
