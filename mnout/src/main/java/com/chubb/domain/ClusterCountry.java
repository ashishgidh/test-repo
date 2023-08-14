package com.chubb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ClusterCountry.
 */
@Document(collection = "cluster_country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClusterCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("name")
    private String name;

    @Field("country")
    @JsonIgnoreProperties(value = { "clusterCountry" }, allowSetters = true)
    private Set<Country> countries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClusterCountry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ClusterCountry name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public void setCountries(Set<Country> countries) {
        if (this.countries != null) {
            this.countries.forEach(i -> i.setClusterCountry(null));
        }
        if (countries != null) {
            countries.forEach(i -> i.setClusterCountry(this));
        }
        this.countries = countries;
    }

    public ClusterCountry countries(Set<Country> countries) {
        this.setCountries(countries);
        return this;
    }

    public ClusterCountry addCountry(Country country) {
        this.countries.add(country);
        return this;
    }

    public ClusterCountry removeCountry(Country country) {
        this.countries.remove(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClusterCountry)) {
            return false;
        }
        return id != null && id.equals(((ClusterCountry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClusterCountry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
