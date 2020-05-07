package com.mobilewebapp.ws.mobilewebappws.service;

import java.util.List;

import com.mobilewebapp.ws.mobilewebappws.dto.AddressDto;
import com.mobilewebapp.ws.mobilewebappws.io.entity.AddressEntity;

public interface AddressService {
	
	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);

}
