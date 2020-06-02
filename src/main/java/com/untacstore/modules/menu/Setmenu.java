package com.untacstore.modules.menu;

import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Menu> menuList = new HashSet<>();

    private Integer totalPrice;

    private String explanation;
}
