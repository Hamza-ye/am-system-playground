package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataInputPeriod.
 */
@Entity
@Table(name = "data_input_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataInputPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "periodType" }, allowSetters = true)
    private Period period;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataInputPeriod id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public DataInputPeriod openingDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosingDate() {
        return this.closingDate;
    }

    public DataInputPeriod closingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public Period getPeriod() {
        return this.period;
    }

    public DataInputPeriod period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataInputPeriod)) {
            return false;
        }
        return id != null && id.equals(((DataInputPeriod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataInputPeriod{" +
            "id=" + getId() +
            ", openingDate='" + getOpeningDate() + "'" +
            ", closingDate='" + getClosingDate() + "'" +
            "}";
    }
}
