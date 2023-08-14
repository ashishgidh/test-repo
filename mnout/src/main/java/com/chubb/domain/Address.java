package com.chubb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Address.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("address_id")
    private Long addressId;

    @Field("street_1")
    private String street1;

    @Field("street_2")
    private String street2;

    @Field("city")
    private String city;

    @Field("country")
    private String country;

    @Field("postal_code")
    private String postalCode;

    @Field("county")
    private String county;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "addresses", "program" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getAddressId() {
        return this.addressId;
    }

    public Address addressId(Long addressId) {
        this.setAddressId(addressId);
        return this;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet1() {
        return this.street1;
    }

    public Address street1(String street1) {
        this.setStreet1(street1);
        return this;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public Address street2(String street2) {
        this.setStreet2(street2);
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Address country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCounty() {
        return this.county;
    }

    public Address county(String county) {
        this.setCounty(county);
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            ", addressId=" + getAddressId() +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", county='" + getCounty() + "'" +
            "}";
    }
}
