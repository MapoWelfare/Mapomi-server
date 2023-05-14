package net.mapomi.mapomi.domain.user;

import lombok.Getter;
import lombok.Setter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.Role;

import javax.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String accountId="";

    @Column
    private String password="";

    @Column
    private String nickName="";

    @Column
    private String picture="";

    @Column
    private boolean certified =false;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String accountId, String password, String nickName, String picture, Role role) {
        this.accountId = accountId;
        this.password = password;
        this.nickName = nickName;
        this.picture = picture;
        this.role = role;
    }

    protected User() {}
}
