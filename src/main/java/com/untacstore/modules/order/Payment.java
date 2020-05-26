package com.untacstore.modules.order;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
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
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Tables tables;

    @ManyToOne
    private Account account;

    @ManyToMany
    private List<Orders> orderList = new ArrayList<>();

    private Integer pay = 0;

    private LocalDateTime paymentAt;

    private LocalDateTime startedAt = null;
}
