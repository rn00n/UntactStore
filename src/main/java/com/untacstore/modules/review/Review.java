package com.untacstore.modules.review;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Review {
    @Id @GeneratedValue
    private Long id;

    @Lob
    private String context;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Account host;

    private LocalDateTime reviewAt;

    private Integer likeReview = 0;//좋아요
//    private Integer report = 0; //신고 TODO 엔티티로 바꿔야함
}
