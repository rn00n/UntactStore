package com.untacstore.modules.order;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.table.Tables;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany
    private Set<Orders> orderList = new HashSet<>();

    private Integer pay = 0;

    private LocalDateTime paymentAt;

    private LocalDateTime startedAt = null;
}
