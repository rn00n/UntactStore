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

    private String title;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToOne
    private Store store;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Menu> menuList = new ArrayList<>();

    private Integer totalPrice;

    private String explanation;
}
