package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.DetailJoinDto;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;

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
        super(dto.getNickname(), "", Role.setRole("abled"));
    }

    protected Abled() {}
}
