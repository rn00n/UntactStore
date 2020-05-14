package com.untacstore.modules.store;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.PrincipalAccount;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.review.Review;
import com.untacstore.modules.table.Tables;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Store {
    @Id @GeneratedValue
    private Long id;

    private String path;

    private String name;

    private String address;

    private String phone;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @ManyToOne
    private Account owner;

    private Integer favorites = 0;

    private Double grade = 0.0;

    private String operatingTime;

    @OneToMany(mappedBy = "store")
    @OrderBy("tableNum")
    private List<Tables> tableList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    @OrderBy("reviewAt")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany
    private List<Setmenu> setmenuList = new ArrayList<>();

    @ManyToMany
    List<Keyword> keywords = new ArrayList<>();

    @ManyToMany
    List<Location> locations = new ArrayList<>();

    private boolean opend;

    //TODO waiting
    public boolean isOwner(PrincipalAccount principalAccount) {
        return this.owner.equals(principalAccount.getAccount());
    }

    public void setOwner(Account account) {
        this.owner = account;
        account.setHasStore(true);
    }
}
