package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import lombok.Setter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.Post;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DiscriminatorValue("abled")
public class Abled extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")
    @Column(name = "user_id")
    private Long id;

    @Column
    private int age;

    @Column
    private String residence=""; //거주지

    @Column
    private int popularity=0;

    public Abled(JoinDto dto) {
        super(dto.getId(), dto.getPassword(), dto.getNickname(), "", Role.setRole("abled"));
        this.age = dto.getAge();
        this.residence = dto.getResidence();
    }

    protected Abled() {}
}
