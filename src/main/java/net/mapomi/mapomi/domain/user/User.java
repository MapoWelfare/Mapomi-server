package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import lombok.Setter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String nickName="";

    @Setter
    @Column
    private String picture="";

    @Column
    private String email="";

    @Column
    private String phoneNum="";

    @Column
    private boolean certified =false;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String nickName, String picture, Role role) {
        this.nickName = nickName;
        this.picture = picture;
        this.role = role;
    }

    public User(String email, String picture) {
        this.email = email;
        this.picture = picture;
    }

    protected User() {}

    public void setJoinInfo(JoinDto dto) {
        this.nickName = dto.getNickname();
        this.phoneNum = dto.getPhoneNum();
    }

}
