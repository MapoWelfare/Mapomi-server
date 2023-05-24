package net.mapomi.mapomi.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.mapomi.mapomi.domain.Enum.MatchRequestStatus;
import net.mapomi.mapomi.domain.user.Abled;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequest {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MatchRequestStatus matchRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Abled abled;


}
