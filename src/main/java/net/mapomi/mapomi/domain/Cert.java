package net.mapomi.mapomi.domain;

import lombok.Getter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.Enum.Status;

import javax.persistence.*;

@Getter
@Entity
public class Cert extends BaseTimeEntity { //대학 학생증인증.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cert_id")
    private Long id;

    @Column
    private String imageURL;  //학생증 url

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.YET;

    @Column
    private Long userId;

    protected Cert() {}
}
