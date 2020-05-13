package com.untacstore.modules.menu;

import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Menu {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    private String name;

    private String price;

}
