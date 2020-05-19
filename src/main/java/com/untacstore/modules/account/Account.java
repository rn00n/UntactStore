package com.untacstore.modules.account;

import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import com.untacstore.modules.waiting.Waiting;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    //TODO keyword
    @ManyToMany
    private List<Keyword> keywords = new ArrayList<>();

    //TODO location 관심지역
    @ManyToMany
    private List<Location> locations = new ArrayList<>();

    //TODO notifications
    private boolean ticketByWeb = true;
//    private boolean ticketByKakao; TODO

    @OneToMany(mappedBy = "account")
    private List<Waiting> waitingList = new ArrayList<>();

    //TODO 신고
    private Integer report = 0;

    public void addReport() {
        report++;
    }

    public boolean hasStore() {
        return hasStore;
    }

//    public boolean hasAvailableWaiting() {
//        return waitingList.stream().filter(w-> w.isAvailable()).collect(Collectors.toList()).isEmpty();
//        return waitingList.stream().filter(w-> w.isAvailable()).collect(Collectors.toList()).isEmpty();
//    }
}
