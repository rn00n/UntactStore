package com.untacstore.modules.menu;

import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Menu {
    @Id @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToOne
    private Store store;

    private String name;

    private Integer price;

    private String explanation;
}
