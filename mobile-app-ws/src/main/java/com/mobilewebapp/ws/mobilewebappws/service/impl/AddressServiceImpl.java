package com.mobilewebapp.ws.mobilewebappws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobilewebapp.ws.mobilewebappws.dto.AddressDto;
import com.mobilewebapp.ws.mobilewebappws.io.entity.AddressEntity;
import com.mobilewebapp.ws.mobilewebappws.io.entity.UserEntity;
import com.mobilewebapp.ws.mobilewebappws.repo.AddressRepository;
import com.mobilewebapp.ws.mobilewebappws.repo.UserRepository;
import com.mobilewebapp.ws.mobilewebappws.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addressRepo;

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnAddress = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			return returnAddress;
		}

		Iterable<AddressEntity> addresses = addressRepo.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnAddress.add(mapper.map(addressEntity, AddressDto.class));
		}

		return returnAddress;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressEntity addressEntity = addressRepo.findByAddressId(addressId);
		ModelMapper mapper = new ModelMapper();
		AddressDto returnAddress = mapper.map(addressEntity, AddressDto.class);
		return returnAddress;
	}

}
