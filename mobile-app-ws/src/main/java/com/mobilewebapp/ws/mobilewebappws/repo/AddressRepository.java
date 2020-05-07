package com.mobilewebapp.ws.mobilewebappws.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobilewebapp.ws.mobilewebappws.io.entity.AddressEntity;
import com.mobilewebapp.ws.mobilewebappws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String addressId);
	
}
