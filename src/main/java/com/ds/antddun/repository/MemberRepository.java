package com.ds.antddun.repository;

import com.ds.antddun.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//CRUD함수를 JpaRepository가 들고 있음
//@Repository라는 어노테이션이 없어도 IoC됨 (Bean으로 등록됨!) Jpa 상속받았기 때문
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where username =:username ")
    Member findByUsername(String username);
}
