package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import lombok.Setter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.dto.request.JoinDto;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(name = "User_SEQ_GEN",sequenceName = "User_SEQ")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")
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
    private Role role = Role.NONE;

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

    public void setJoinInfo(String type, JoinDto dto) {
        this.nickName = dto.getNickname();
        this.phoneNum = dto.getPhoneNum();
        if(type.equals("disabled"))
            this.role = Role.DISABLED;
        else if(type.equals("abled"))
            this.role = Role.ABLED;
        else
            this.role = Role.OBSERVER;
    }

}
