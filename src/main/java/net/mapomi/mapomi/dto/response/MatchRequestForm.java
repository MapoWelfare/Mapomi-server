package net.mapomi.mapomi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchRequestForm {

    private long matchRequestId;
    private String nickName;
    private String picture;
}
