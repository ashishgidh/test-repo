package com.chubb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Client.
 */
@Document(collection = "client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("client_id")
    private Long clientId;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("company")
    private String company;

    @Field("address")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @DBRef
    @Field("program")
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Program program;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Client id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public Client clientId(Long clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return this.name;
    }

    public Client name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Client phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return this.company;
    }

    public Client company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setClient(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setClient(this));
        }
        this.addresses = addresses;
    }

    public Client addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Client addAddress(Address address) {
        this.addresses.add(address);
        return this;
    }

    public Client removeAddress(Address address) {
        this.addresses.remove(address);
        return this;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Client program(Program program) {
        this.setProgram(program);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", clientId=" + getClientId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }
}
