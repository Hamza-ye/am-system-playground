package org.nmcpye.activitiesmanagement.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkingDay.
 */
@Entity
@Table(name = "working_day")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkingDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "day_no", nullable = false, unique = true)
    private Integer dayNo;

    @NotNull
    @Size(max = 20)
    @Column(name = "day_label", length = 20, nullable = false, unique = true)
    private String dayLabel;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkingDay id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getDayNo() {
        return this.dayNo;
    }

    public WorkingDay dayNo(Integer dayNo) {
        this.dayNo = dayNo;
        return this;
    }

    public void setDayNo(Integer dayNo) {
        this.dayNo = dayNo;
    }

    public String getDayLabel() {
        return this.dayLabel;
    }

    public WorkingDay dayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
        return this;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingDay)) {
            return false;
        }
        return id != null && id.equals(((WorkingDay) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingDay{" +
            "id=" + getId() +
            ", dayNo=" + getDayNo() +
            ", dayLabel='" + getDayLabel() + "'" +
            "}";
    }
}
