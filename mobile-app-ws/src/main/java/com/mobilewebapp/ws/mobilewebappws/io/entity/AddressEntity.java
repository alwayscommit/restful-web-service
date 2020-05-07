package com.mobilewebapp.ws.mobilewebappws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "address")
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = -1264288698565122246L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(length=30, nullable=false)
	private String addressId; //public id
	@Column(length=20, nullable=false)
	private String city;
	@Column(length=20, nullable=false)
	private String country;
	@Column(length=100, nullable=false)
	private String streetName;
	@Column(length=6, nullable=false)
	private String postalCode;
	@Column(length=10, nullable=false)
	private String type;
	@ManyToOne
	@JoinColumn(name="users_id")//tablename_fieldname
	private UserEntity userDetails;
	
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

}
