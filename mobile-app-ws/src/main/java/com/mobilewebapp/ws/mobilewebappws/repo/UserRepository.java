package com.mobilewebapp.ws.mobilewebappws.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mobilewebapp.ws.mobilewebappws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);

	UserEntity findByUserId(String id);

	UserEntity findUserByEmailVerificationToken(String token);

}
