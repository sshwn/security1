package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 IoC가 된다. 이유는 JpaRepository를 상속받았기때문에
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy 까지는 규칙 -> Username은 문법
    // select * from user where username = ? 가 호출된다.
    public User findByUsername(String username);    // Jpa Query methods 함수

//    select * from user Where email = ?
//    public User findByEmail();
}
