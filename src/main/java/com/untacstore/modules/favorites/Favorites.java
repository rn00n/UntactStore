package com.untacstore.modules.favorites;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity @EqualsAndHashCode(of = "id")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Favorites {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Account account; //사용자

    @ManyToOne
    private Store store; //가게
}
