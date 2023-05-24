package net.mapomi.mapomi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.mapomi.mapomi.domain.Role;

@Getter
@Builder
@AllArgsConstructor
public class ProfileForm {
    private String nickName;
    private String picture;
    private Role role;

}
