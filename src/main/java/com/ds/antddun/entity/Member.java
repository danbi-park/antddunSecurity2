package com.ds.antddun.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; //email
    private String name;
    private String password;
    private String role;

    //구글 로그인 위함 / 그냥 로그인을 구분하기 위해
    private String provider;
    private String providerId;

    @CreationTimestamp
    private LocalDateTime createDateTime;

/*    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AntMemberRoleSet> roleSet = new HashSet<>();

    public void addMemberRole(AntMemberRoleSet userRole){
        roleSet.add(userRole);
    }*/
}
