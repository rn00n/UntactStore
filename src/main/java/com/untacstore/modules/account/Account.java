package com.untacstore.modules.account;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    //TODO keyword
    //TODO address
}
