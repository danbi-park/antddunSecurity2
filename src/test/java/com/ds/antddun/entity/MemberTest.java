package com.ds.antddun.entity;

import com.ds.antddun.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository repository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,3).forEach( v-> {

        });
    }

}