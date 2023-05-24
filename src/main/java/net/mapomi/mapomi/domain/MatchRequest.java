package net.mapomi.mapomi.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.mapomi.mapomi.domain.Enum.MatchRequestStatus;
import net.mapomi.mapomi.domain.user.Abled;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MatchRequest {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MatchRequestStatus matchRequestStatus = MatchRequestStatus.YET;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accompany accompany;

    @ManyToOne(fetch = FetchType.LAZY)
    private Abled abled;

    public void setStatus(MatchRequestStatus matchRequestStatus){
        this.matchRequestStatus = matchRequestStatus;
    }


}
