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
 * A Program.
 */
@Document(collection = "program")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("program_id")
    private Long programId;

    @Field("cluster_id")
    private Long clusterId;

    @Field("country_id")
    private Long countryId;

    @Field("branch_id")
    private Long branchId;

    @Field("max_lob_id")
    private Long maxLOBId;

    @Field("wv_lob_id")
    private Long wvLOBId;

    @Field("program_effective_date")
    private String programEffectiveDate;

    @Field("program_expiry_date")
    private String programExpiryDate;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "addresses", "program" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Program id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProgramId() {
        return this.programId;
    }

    public Program programId(Long programId) {
        this.setProgramId(programId);
        return this;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getClusterId() {
        return this.clusterId;
    }

    public Program clusterId(Long clusterId) {
        this.setClusterId(clusterId);
        return this;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public Program countryId(Long countryId) {
        this.setCountryId(countryId);
        return this;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getBranchId() {
        return this.branchId;
    }

    public Program branchId(Long branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getMaxLOBId() {
        return this.maxLOBId;
    }

    public Program maxLOBId(Long maxLOBId) {
        this.setMaxLOBId(maxLOBId);
        return this;
    }

    public void setMaxLOBId(Long maxLOBId) {
        this.maxLOBId = maxLOBId;
    }

    public Long getWvLOBId() {
        return this.wvLOBId;
    }

    public Program wvLOBId(Long wvLOBId) {
        this.setWvLOBId(wvLOBId);
        return this;
    }

    public void setWvLOBId(Long wvLOBId) {
        this.wvLOBId = wvLOBId;
    }

    public String getProgramEffectiveDate() {
        return this.programEffectiveDate;
    }

    public Program programEffectiveDate(String programEffectiveDate) {
        this.setProgramEffectiveDate(programEffectiveDate);
        return this;
    }

    public void setProgramEffectiveDate(String programEffectiveDate) {
        this.programEffectiveDate = programEffectiveDate;
    }

    public String getProgramExpiryDate() {
        return this.programExpiryDate;
    }

    public Program programExpiryDate(String programExpiryDate) {
        this.setProgramExpiryDate(programExpiryDate);
        return this;
    }

    public void setProgramExpiryDate(String programExpiryDate) {
        this.programExpiryDate = programExpiryDate;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setProgram(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setProgram(this));
        }
        this.clients = clients;
    }

    public Program clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Program addClient(Client client) {
        this.clients.add(client);
        client.setProgram(this);
        return this;
    }

    public Program removeClient(Client client) {
        this.clients.remove(client);
        client.setProgram(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Program)) {
            return false;
        }
        return id != null && id.equals(((Program) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", programId=" + getProgramId() +
            ", clusterId=" + getClusterId() +
            ", countryId=" + getCountryId() +
            ", branchId=" + getBranchId() +
            ", maxLOBId=" + getMaxLOBId() +
            ", wvLOBId=" + getWvLOBId() +
            ", programEffectiveDate='" + getProgramEffectiveDate() + "'" +
            ", programExpiryDate='" + getProgramExpiryDate() + "'" +
            "}";
    }
}
