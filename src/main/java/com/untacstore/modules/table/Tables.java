package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.order.Orders;
import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Tables {
    @Id @GeneratedValue
    private Long id;

    private Integer tableNum;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "tables")
    @OrderBy("orderAt")
    private List<Orders> orderList = new ArrayList<>(); //주문목록


}
