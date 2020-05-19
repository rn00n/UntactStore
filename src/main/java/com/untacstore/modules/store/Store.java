package com.untacstore.modules.store;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.PrincipalAccount;
import com.untacstore.modules.favorites.Favorites;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.review.Review;
import com.untacstore.modules.table.Tables;
import com.untacstore.modules.waiting.Waiting;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Store {
    @Id @GeneratedValue
    private Long id;

    private String licensee; //사업자등록번호

    private String path;

    private String name;

    private String address;

    private String phone;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @ManyToOne
    private Account owner;

    private Double grade = 0.0;

    private String operatingTime;

    @OneToMany(mappedBy = "store")
    @OrderBy("tableNum")
    private List<Tables> tableList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    @OrderBy("reviewAt")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Setmenu> setmenuList = new ArrayList<>();

    @ManyToMany
    List<Keyword> keywords = new ArrayList<>();

    private boolean open = true; //TODO
    private boolean waiting = true; //TODO

    @OneToMany(mappedBy = "store")
    @OrderBy("turn")
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Favorites> favoritesList = new ArrayList<>();

    //TODO waiting
    public boolean isOwner(PrincipalAccount principalAccount) {
        return this.owner.equals(principalAccount.getAccount());
    }

    public void setOwner(Account account) {
        this.owner = account;
        account.setHasStore(true);
    }

    public void addMenu(Menu newMenu) {
        menuList.add(newMenu);
        newMenu.setStore(this);
    }

    public void removeMenu(Menu menu) {
        menuList.remove(menu);
    }

    public void addSetmenu(Setmenu newSetmenu) {
        setmenuList.add(newSetmenu);
        newSetmenu.setStore(this);
    }

    public void removeSetmenu(Setmenu setmenu) {
        setmenuList.remove(setmenu);
    }


    public void addTable(Tables tables) {
        this.tableList.add(tables);
        tables.setStore(this);
    }

    public Tables removeTable() {
        Tables tables = tableList.remove(tableList.size()-1);
        return tables;
    }

    public boolean isWaitingable(PrincipalAccount principalAccount) {
        Account account = principalAccount.getAccount();

        return waitingList.stream().map(Waiting::getAccount).filter(a -> a.equals(account)).collect(Collectors.toList()).isEmpty();
    }

    public boolean isFavoritesable(PrincipalAccount principalAccount) {
        Account account = principalAccount.getAccount();

        return favoritesList.stream().map(Favorites::getAccount).filter(a -> a.equals(account)).collect(Collectors.toList()).isEmpty();
    }

    public void addWaiting(Waiting waiting) {
        this.waitingList.add(waiting);
        waiting.setStore(this);
    }

    public void removeWaiting(Waiting waiting) {
        this.waitingList.remove(waiting);
    }

}
