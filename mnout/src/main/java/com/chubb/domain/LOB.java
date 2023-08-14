package com.chubb.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A LOB.
 */
@Document(collection = "lob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LOB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("wv_lob_code")
    private String wvLobCode;

    @Field("wv_lob_name")
    private String wvLobName;

    @Field("max_lob_code")
    private String maxLobCode;

    @Field("max_lob_name")
    private String maxLobName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LOB id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWvLobCode() {
        return this.wvLobCode;
    }

    public LOB wvLobCode(String wvLobCode) {
        this.setWvLobCode(wvLobCode);
        return this;
    }

    public void setWvLobCode(String wvLobCode) {
        this.wvLobCode = wvLobCode;
    }

    public String getWvLobName() {
        return this.wvLobName;
    }

    public LOB wvLobName(String wvLobName) {
        this.setWvLobName(wvLobName);
        return this;
    }

    public void setWvLobName(String wvLobName) {
        this.wvLobName = wvLobName;
    }

    public String getMaxLobCode() {
        return this.maxLobCode;
    }

    public LOB maxLobCode(String maxLobCode) {
        this.setMaxLobCode(maxLobCode);
        return this;
    }

    public void setMaxLobCode(String maxLobCode) {
        this.maxLobCode = maxLobCode;
    }

    public String getMaxLobName() {
        return this.maxLobName;
    }

    public LOB maxLobName(String maxLobName) {
        this.setMaxLobName(maxLobName);
        return this;
    }

    public void setMaxLobName(String maxLobName) {
        this.maxLobName = maxLobName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LOB)) {
            return false;
        }
        return id != null && id.equals(((LOB) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LOB{" +
            "id=" + getId() +
            ", wvLobCode='" + getWvLobCode() + "'" +
            ", wvLobName='" + getWvLobName() + "'" +
            ", maxLobCode='" + getMaxLobCode() + "'" +
            ", maxLobName='" + getMaxLobName() + "'" +
            "}";
    }
}
