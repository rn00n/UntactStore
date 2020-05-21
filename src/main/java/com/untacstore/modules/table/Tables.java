package com.untacstore.modules.table;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.PrincipalAccount;
import com.untacstore.modules.order.Orders;
import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Tables {
    @Id @GeneratedValue
    private Long id;

    private Integer tableNum;

    private String tablesPath;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Account account; //현재 손님

    private Integer personnel;

    private LocalDateTime startedAt = null;

    private LocalDateTime endAt = null;

    private Integer amount = null; //가격

    @OneToMany(mappedBy = "tables")
    @OrderBy("orderAt")
    private List<Orders> orderList = new ArrayList<>(); //주문목록

    public boolean isSitable(PrincipalAccount principalAccount) {
        return this.account == null;
    }

    public boolean isAdmin(Store store) {
        return this.store.equals(store);
    }
}
