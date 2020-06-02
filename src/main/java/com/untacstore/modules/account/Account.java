package com.untacstore.modules.account;

import com.untacstore.modules.account.authentication.PrincipalAccount;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import com.untacstore.modules.waiting.Waiting;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;

    @Column(unique = true)
    private String email;
    private boolean emailVerified; //이메일 검증 확인
    private String emailCheckToken; //이메일 검증 토큰
    private LocalDateTime emailVerifiedDateTime; //회원가입 시간

    private boolean hasStore = false;

    //TODO profileImage;
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    //keyword
    @ManyToMany
    private Set<Keyword> keywords = new HashSet<>();

    //location 관심지역
    @ManyToMany
    private Set<Location> locations = new HashSet<>();

    //TODO notifications
    private boolean storeCreatedByWeb = true;
    private boolean ticketByWeb = true;
//    private boolean ticketByKakao; TODO

    @OneToMany(mappedBy = "account")
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "to")
    private List<AccountReport> report = new ArrayList<>();

    public int reportSize() {
        return report.size();
    }

    public boolean isReportable(PrincipalAccount principalAccount) {
        Account account = principalAccount.getAccount();
        return report.stream().filter(r->r.getFrom().equals(account)).collect(Collectors.toList()).isEmpty();
    }

    public boolean hasStore() {
        return hasStore;
    }

//    public boolean hasAvailableWaiting() {
//        return waitingList.stream().filter(w-> w.isAvailable()).collect(Collectors.toList()).isEmpty();
//        return waitingList.stream().filter(w-> w.isAvailable()).collect(Collectors.toList()).isEmpty();
//    }
}
