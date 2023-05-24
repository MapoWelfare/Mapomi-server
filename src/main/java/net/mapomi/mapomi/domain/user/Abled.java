package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import net.mapomi.mapomi.domain.MatchRequest;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.DetailJoinDto;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "abled",cascade = CascadeType.MERGE)
    private Set<MatchRequest> matchRequests = new LinkedHashSet<>();



    public Abled(JoinDto dto) {
        super(dto.getNickname(), "", Role.setRole("abled"));
    }

    protected Abled() {}
}
