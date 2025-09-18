package com.shsb.atfbook.domain.category;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @Column(name="sub_cid")
    private String subCid;
    @Column
    private String depth1;
}
