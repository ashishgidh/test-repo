package com.chubb.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PortfolioSegment.
 */
@Document(collection = "portfolio_segment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PortfolioSegment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("portfolio_sigment")
    private Long portfolioSigment;

    @Field("name")
    private String name;

    @Field("segment")
    private String segment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PortfolioSegment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPortfolioSigment() {
        return this.portfolioSigment;
    }

    public PortfolioSegment portfolioSigment(Long portfolioSigment) {
        this.setPortfolioSigment(portfolioSigment);
        return this;
    }

    public void setPortfolioSigment(Long portfolioSigment) {
        this.portfolioSigment = portfolioSigment;
    }

    public String getName() {
        return this.name;
    }

    public PortfolioSegment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSegment() {
        return this.segment;
    }

    public PortfolioSegment segment(String segment) {
        this.setSegment(segment);
        return this;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortfolioSegment)) {
            return false;
        }
        return id != null && id.equals(((PortfolioSegment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortfolioSegment{" +
            "id=" + getId() +
            ", portfolioSigment=" + getPortfolioSigment() +
            ", name='" + getName() + "'" +
            ", segment='" + getSegment() + "'" +
            "}";
    }
}
