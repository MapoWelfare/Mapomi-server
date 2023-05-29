package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.DetailJoinDto;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;


@Getter
@Entity
@DiscriminatorValue("observer")
public class Observer extends User{

    @OneToOne
    @JoinColumn(name = "disabled_id")
    private Disabled disabled;

    protected Observer() {}
}
