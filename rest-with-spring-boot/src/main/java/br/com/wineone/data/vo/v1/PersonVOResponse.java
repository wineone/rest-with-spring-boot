package br.com.wineone.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;



@JsonPropertyOrder({"id","address","firstName","lasName","gender"})
public class PersonVOResponse extends RepresentationModel<PersonVOResponse> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Mapping("id")
	@JsonProperty("id")
	private Long key;
//	@JsonProperty("first_name")
	private String firstName;
//	@JsonProperty("last_name")
	private String lastName;
	private String address;
//	@JsonIgnore()
	private String gender;
	
	private boolean enabled;
	
	
	public PersonVOResponse() {
		super();
	}
	

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(address, enabled, firstName, gender, key, lastName);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonVOResponse other = (PersonVOResponse) obj;
		return Objects.equals(address, other.address) && enabled == other.enabled
				&& Objects.equals(firstName, other.firstName) && Objects.equals(gender, other.gender)
				&& Objects.equals(key, other.key) && Objects.equals(lastName, other.lastName);
	}
}
