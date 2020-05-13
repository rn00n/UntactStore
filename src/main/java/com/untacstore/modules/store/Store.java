package com.untacstore.modules.store;

import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
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

    private String name;

    private String address;

    private Integer favorites = 0;

    private Double grade = 0.0;

    @OneToMany(mappedBy = "store")
    @OrderBy("tableNum")
    private List<Tables> tableList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    @OrderBy("reviewAt")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany
    List<Keyword> keywords = new ArrayList<>();

    @ManyToMany
    List<Location> locations = new ArrayList<>();
}
