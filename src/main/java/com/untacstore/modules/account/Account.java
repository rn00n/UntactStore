package com.untacstore.modules.account;

import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String licensee; //사업자등록번호
    @Enumerated(value = EnumType.STRING)
    private AccountType accountType; //계정 유형

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

    //TODO 신고
    private Integer report = 0;

    public void addReport() {
        report++;
    }
}
