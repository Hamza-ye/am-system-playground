package org.nmcpye.activitiesmanagement.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RelativePeriods.
 */
@Entity
@Table(name = "relative_periods")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RelativePeriods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "this_day")
    private Boolean thisDay;

    @Column(name = "yesterday")
    private Boolean yesterday;

    @Column(name = "last_3_days")
    private Boolean last3Days;

    @Column(name = "last_7_days")
    private Boolean last7Days;

    @Column(name = "last_14_days")
    private Boolean last14Days;

    @Column(name = "this_month")
    private Boolean thisMonth;

    @Column(name = "last_month")
    private Boolean lastMonth;

    @Column(name = "this_bimonth")
    private Boolean thisBimonth;

    @Column(name = "last_bimonth")
    private Boolean lastBimonth;

    @Column(name = "this_quarter")
    private Boolean thisQuarter;

    @Column(name = "last_quarter")
    private Boolean lastQuarter;

    @Column(name = "this_six_month")
    private Boolean thisSixMonth;

    @Column(name = "last_six_month")
    private Boolean lastSixMonth;

    @Column(name = "weeks_this_year")
    private Boolean weeksThisYear;

    @Column(name = "months_this_year")
    private Boolean monthsThisYear;

    @Column(name = "bi_months_this_year")
    private Boolean biMonthsThisYear;

    @Column(name = "quarters_this_year")
    private Boolean quartersThisYear;

    @Column(name = "this_year")
    private Boolean thisYear;

    @Column(name = "months_last_year")
    private Boolean monthsLastYear;

    @Column(name = "quarters_last_year")
    private Boolean quartersLastYear;

    @Column(name = "last_year")
    private Boolean lastYear;

    @Column(name = "last_5_years")
    private Boolean last5Years;

    @Column(name = "last_12_months")
    private Boolean last12Months;

    @Column(name = "last_6_months")
    private Boolean last6Months;

    @Column(name = "last_3_months")
    private Boolean last3Months;

    @Column(name = "last_6_bi_months")
    private Boolean last6BiMonths;

    @Column(name = "last_4_quarters")
    private Boolean last4Quarters;

    @Column(name = "last_2_six_months")
    private Boolean last2SixMonths;

    @Column(name = "this_financial_year")
    private Boolean thisFinancialYear;

    @Column(name = "last_financial_year")
    private Boolean lastFinancialYear;

    @Column(name = "last_5_financial_years")
    private Boolean last5FinancialYears;

    @Column(name = "this_week")
    private Boolean thisWeek;

    @Column(name = "last_week")
    private Boolean lastWeek;

    @Column(name = "this_bi_week")
    private Boolean thisBiWeek;

    @Column(name = "last_bi_week")
    private Boolean lastBiWeek;

    @Column(name = "last_4_weeks")
    private Boolean last4Weeks;

    @Column(name = "last_4_bi_weeks")
    private Boolean last4BiWeeks;

    @Column(name = "last_12_weeks")
    private Boolean last12Weeks;

    @Column(name = "last_52_weeks")
    private Boolean last52Weeks;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelativePeriods id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getThisDay() {
        return this.thisDay;
    }

    public RelativePeriods thisDay(Boolean thisDay) {
        this.thisDay = thisDay;
        return this;
    }

    public void setThisDay(Boolean thisDay) {
        this.thisDay = thisDay;
    }

    public Boolean getYesterday() {
        return this.yesterday;
    }

    public RelativePeriods yesterday(Boolean yesterday) {
        this.yesterday = yesterday;
        return this;
    }

    public void setYesterday(Boolean yesterday) {
        this.yesterday = yesterday;
    }

    public Boolean getLast3Days() {
        return this.last3Days;
    }

    public RelativePeriods last3Days(Boolean last3Days) {
        this.last3Days = last3Days;
        return this;
    }

    public void setLast3Days(Boolean last3Days) {
        this.last3Days = last3Days;
    }

    public Boolean getLast7Days() {
        return this.last7Days;
    }

    public RelativePeriods last7Days(Boolean last7Days) {
        this.last7Days = last7Days;
        return this;
    }

    public void setLast7Days(Boolean last7Days) {
        this.last7Days = last7Days;
    }

    public Boolean getLast14Days() {
        return this.last14Days;
    }

    public RelativePeriods last14Days(Boolean last14Days) {
        this.last14Days = last14Days;
        return this;
    }

    public void setLast14Days(Boolean last14Days) {
        this.last14Days = last14Days;
    }

    public Boolean getThisMonth() {
        return this.thisMonth;
    }

    public RelativePeriods thisMonth(Boolean thisMonth) {
        this.thisMonth = thisMonth;
        return this;
    }

    public void setThisMonth(Boolean thisMonth) {
        this.thisMonth = thisMonth;
    }

    public Boolean getLastMonth() {
        return this.lastMonth;
    }

    public RelativePeriods lastMonth(Boolean lastMonth) {
        this.lastMonth = lastMonth;
        return this;
    }

    public void setLastMonth(Boolean lastMonth) {
        this.lastMonth = lastMonth;
    }

    public Boolean getThisBimonth() {
        return this.thisBimonth;
    }

    public RelativePeriods thisBimonth(Boolean thisBimonth) {
        this.thisBimonth = thisBimonth;
        return this;
    }

    public void setThisBimonth(Boolean thisBimonth) {
        this.thisBimonth = thisBimonth;
    }

    public Boolean getLastBimonth() {
        return this.lastBimonth;
    }

    public RelativePeriods lastBimonth(Boolean lastBimonth) {
        this.lastBimonth = lastBimonth;
        return this;
    }

    public void setLastBimonth(Boolean lastBimonth) {
        this.lastBimonth = lastBimonth;
    }

    public Boolean getThisQuarter() {
        return this.thisQuarter;
    }

    public RelativePeriods thisQuarter(Boolean thisQuarter) {
        this.thisQuarter = thisQuarter;
        return this;
    }

    public void setThisQuarter(Boolean thisQuarter) {
        this.thisQuarter = thisQuarter;
    }

    public Boolean getLastQuarter() {
        return this.lastQuarter;
    }

    public RelativePeriods lastQuarter(Boolean lastQuarter) {
        this.lastQuarter = lastQuarter;
        return this;
    }

    public void setLastQuarter(Boolean lastQuarter) {
        this.lastQuarter = lastQuarter;
    }

    public Boolean getThisSixMonth() {
        return this.thisSixMonth;
    }

    public RelativePeriods thisSixMonth(Boolean thisSixMonth) {
        this.thisSixMonth = thisSixMonth;
        return this;
    }

    public void setThisSixMonth(Boolean thisSixMonth) {
        this.thisSixMonth = thisSixMonth;
    }

    public Boolean getLastSixMonth() {
        return this.lastSixMonth;
    }

    public RelativePeriods lastSixMonth(Boolean lastSixMonth) {
        this.lastSixMonth = lastSixMonth;
        return this;
    }

    public void setLastSixMonth(Boolean lastSixMonth) {
        this.lastSixMonth = lastSixMonth;
    }

    public Boolean getWeeksThisYear() {
        return this.weeksThisYear;
    }

    public RelativePeriods weeksThisYear(Boolean weeksThisYear) {
        this.weeksThisYear = weeksThisYear;
        return this;
    }

    public void setWeeksThisYear(Boolean weeksThisYear) {
        this.weeksThisYear = weeksThisYear;
    }

    public Boolean getMonthsThisYear() {
        return this.monthsThisYear;
    }

    public RelativePeriods monthsThisYear(Boolean monthsThisYear) {
        this.monthsThisYear = monthsThisYear;
        return this;
    }

    public void setMonthsThisYear(Boolean monthsThisYear) {
        this.monthsThisYear = monthsThisYear;
    }

    public Boolean getBiMonthsThisYear() {
        return this.biMonthsThisYear;
    }

    public RelativePeriods biMonthsThisYear(Boolean biMonthsThisYear) {
        this.biMonthsThisYear = biMonthsThisYear;
        return this;
    }

    public void setBiMonthsThisYear(Boolean biMonthsThisYear) {
        this.biMonthsThisYear = biMonthsThisYear;
    }

    public Boolean getQuartersThisYear() {
        return this.quartersThisYear;
    }

    public RelativePeriods quartersThisYear(Boolean quartersThisYear) {
        this.quartersThisYear = quartersThisYear;
        return this;
    }

    public void setQuartersThisYear(Boolean quartersThisYear) {
        this.quartersThisYear = quartersThisYear;
    }

    public Boolean getThisYear() {
        return this.thisYear;
    }

    public RelativePeriods thisYear(Boolean thisYear) {
        this.thisYear = thisYear;
        return this;
    }

    public void setThisYear(Boolean thisYear) {
        this.thisYear = thisYear;
    }

    public Boolean getMonthsLastYear() {
        return this.monthsLastYear;
    }

    public RelativePeriods monthsLastYear(Boolean monthsLastYear) {
        this.monthsLastYear = monthsLastYear;
        return this;
    }

    public void setMonthsLastYear(Boolean monthsLastYear) {
        this.monthsLastYear = monthsLastYear;
    }

    public Boolean getQuartersLastYear() {
        return this.quartersLastYear;
    }

    public RelativePeriods quartersLastYear(Boolean quartersLastYear) {
        this.quartersLastYear = quartersLastYear;
        return this;
    }

    public void setQuartersLastYear(Boolean quartersLastYear) {
        this.quartersLastYear = quartersLastYear;
    }

    public Boolean getLastYear() {
        return this.lastYear;
    }

    public RelativePeriods lastYear(Boolean lastYear) {
        this.lastYear = lastYear;
        return this;
    }

    public void setLastYear(Boolean lastYear) {
        this.lastYear = lastYear;
    }

    public Boolean getLast5Years() {
        return this.last5Years;
    }

    public RelativePeriods last5Years(Boolean last5Years) {
        this.last5Years = last5Years;
        return this;
    }

    public void setLast5Years(Boolean last5Years) {
        this.last5Years = last5Years;
    }

    public Boolean getLast12Months() {
        return this.last12Months;
    }

    public RelativePeriods last12Months(Boolean last12Months) {
        this.last12Months = last12Months;
        return this;
    }

    public void setLast12Months(Boolean last12Months) {
        this.last12Months = last12Months;
    }

    public Boolean getLast6Months() {
        return this.last6Months;
    }

    public RelativePeriods last6Months(Boolean last6Months) {
        this.last6Months = last6Months;
        return this;
    }

    public void setLast6Months(Boolean last6Months) {
        this.last6Months = last6Months;
    }

    public Boolean getLast3Months() {
        return this.last3Months;
    }

    public RelativePeriods last3Months(Boolean last3Months) {
        this.last3Months = last3Months;
        return this;
    }

    public void setLast3Months(Boolean last3Months) {
        this.last3Months = last3Months;
    }

    public Boolean getLast6BiMonths() {
        return this.last6BiMonths;
    }

    public RelativePeriods last6BiMonths(Boolean last6BiMonths) {
        this.last6BiMonths = last6BiMonths;
        return this;
    }

    public void setLast6BiMonths(Boolean last6BiMonths) {
        this.last6BiMonths = last6BiMonths;
    }

    public Boolean getLast4Quarters() {
        return this.last4Quarters;
    }

    public RelativePeriods last4Quarters(Boolean last4Quarters) {
        this.last4Quarters = last4Quarters;
        return this;
    }

    public void setLast4Quarters(Boolean last4Quarters) {
        this.last4Quarters = last4Quarters;
    }

    public Boolean getLast2SixMonths() {
        return this.last2SixMonths;
    }

    public RelativePeriods last2SixMonths(Boolean last2SixMonths) {
        this.last2SixMonths = last2SixMonths;
        return this;
    }

    public void setLast2SixMonths(Boolean last2SixMonths) {
        this.last2SixMonths = last2SixMonths;
    }

    public Boolean getThisFinancialYear() {
        return this.thisFinancialYear;
    }

    public RelativePeriods thisFinancialYear(Boolean thisFinancialYear) {
        this.thisFinancialYear = thisFinancialYear;
        return this;
    }

    public void setThisFinancialYear(Boolean thisFinancialYear) {
        this.thisFinancialYear = thisFinancialYear;
    }

    public Boolean getLastFinancialYear() {
        return this.lastFinancialYear;
    }

    public RelativePeriods lastFinancialYear(Boolean lastFinancialYear) {
        this.lastFinancialYear = lastFinancialYear;
        return this;
    }

    public void setLastFinancialYear(Boolean lastFinancialYear) {
        this.lastFinancialYear = lastFinancialYear;
    }

    public Boolean getLast5FinancialYears() {
        return this.last5FinancialYears;
    }

    public RelativePeriods last5FinancialYears(Boolean last5FinancialYears) {
        this.last5FinancialYears = last5FinancialYears;
        return this;
    }

    public void setLast5FinancialYears(Boolean last5FinancialYears) {
        this.last5FinancialYears = last5FinancialYears;
    }

    public Boolean getThisWeek() {
        return this.thisWeek;
    }

    public RelativePeriods thisWeek(Boolean thisWeek) {
        this.thisWeek = thisWeek;
        return this;
    }

    public void setThisWeek(Boolean thisWeek) {
        this.thisWeek = thisWeek;
    }

    public Boolean getLastWeek() {
        return this.lastWeek;
    }

    public RelativePeriods lastWeek(Boolean lastWeek) {
        this.lastWeek = lastWeek;
        return this;
    }

    public void setLastWeek(Boolean lastWeek) {
        this.lastWeek = lastWeek;
    }

    public Boolean getThisBiWeek() {
        return this.thisBiWeek;
    }

    public RelativePeriods thisBiWeek(Boolean thisBiWeek) {
        this.thisBiWeek = thisBiWeek;
        return this;
    }

    public void setThisBiWeek(Boolean thisBiWeek) {
        this.thisBiWeek = thisBiWeek;
    }

    public Boolean getLastBiWeek() {
        return this.lastBiWeek;
    }

    public RelativePeriods lastBiWeek(Boolean lastBiWeek) {
        this.lastBiWeek = lastBiWeek;
        return this;
    }

    public void setLastBiWeek(Boolean lastBiWeek) {
        this.lastBiWeek = lastBiWeek;
    }

    public Boolean getLast4Weeks() {
        return this.last4Weeks;
    }

    public RelativePeriods last4Weeks(Boolean last4Weeks) {
        this.last4Weeks = last4Weeks;
        return this;
    }

    public void setLast4Weeks(Boolean last4Weeks) {
        this.last4Weeks = last4Weeks;
    }

    public Boolean getLast4BiWeeks() {
        return this.last4BiWeeks;
    }

    public RelativePeriods last4BiWeeks(Boolean last4BiWeeks) {
        this.last4BiWeeks = last4BiWeeks;
        return this;
    }

    public void setLast4BiWeeks(Boolean last4BiWeeks) {
        this.last4BiWeeks = last4BiWeeks;
    }

    public Boolean getLast12Weeks() {
        return this.last12Weeks;
    }

    public RelativePeriods last12Weeks(Boolean last12Weeks) {
        this.last12Weeks = last12Weeks;
        return this;
    }

    public void setLast12Weeks(Boolean last12Weeks) {
        this.last12Weeks = last12Weeks;
    }

    public Boolean getLast52Weeks() {
        return this.last52Weeks;
    }

    public RelativePeriods last52Weeks(Boolean last52Weeks) {
        this.last52Weeks = last52Weeks;
        return this;
    }

    public void setLast52Weeks(Boolean last52Weeks) {
        this.last52Weeks = last52Weeks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelativePeriods)) {
            return false;
        }
        return id != null && id.equals(((RelativePeriods) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelativePeriods{" +
            "id=" + getId() +
            ", thisDay='" + getThisDay() + "'" +
            ", yesterday='" + getYesterday() + "'" +
            ", last3Days='" + getLast3Days() + "'" +
            ", last7Days='" + getLast7Days() + "'" +
            ", last14Days='" + getLast14Days() + "'" +
            ", thisMonth='" + getThisMonth() + "'" +
            ", lastMonth='" + getLastMonth() + "'" +
            ", thisBimonth='" + getThisBimonth() + "'" +
            ", lastBimonth='" + getLastBimonth() + "'" +
            ", thisQuarter='" + getThisQuarter() + "'" +
            ", lastQuarter='" + getLastQuarter() + "'" +
            ", thisSixMonth='" + getThisSixMonth() + "'" +
            ", lastSixMonth='" + getLastSixMonth() + "'" +
            ", weeksThisYear='" + getWeeksThisYear() + "'" +
            ", monthsThisYear='" + getMonthsThisYear() + "'" +
            ", biMonthsThisYear='" + getBiMonthsThisYear() + "'" +
            ", quartersThisYear='" + getQuartersThisYear() + "'" +
            ", thisYear='" + getThisYear() + "'" +
            ", monthsLastYear='" + getMonthsLastYear() + "'" +
            ", quartersLastYear='" + getQuartersLastYear() + "'" +
            ", lastYear='" + getLastYear() + "'" +
            ", last5Years='" + getLast5Years() + "'" +
            ", last12Months='" + getLast12Months() + "'" +
            ", last6Months='" + getLast6Months() + "'" +
            ", last3Months='" + getLast3Months() + "'" +
            ", last6BiMonths='" + getLast6BiMonths() + "'" +
            ", last4Quarters='" + getLast4Quarters() + "'" +
            ", last2SixMonths='" + getLast2SixMonths() + "'" +
            ", thisFinancialYear='" + getThisFinancialYear() + "'" +
            ", lastFinancialYear='" + getLastFinancialYear() + "'" +
            ", last5FinancialYears='" + getLast5FinancialYears() + "'" +
            ", thisWeek='" + getThisWeek() + "'" +
            ", lastWeek='" + getLastWeek() + "'" +
            ", thisBiWeek='" + getThisBiWeek() + "'" +
            ", lastBiWeek='" + getLastBiWeek() + "'" +
            ", last4Weeks='" + getLast4Weeks() + "'" +
            ", last4BiWeeks='" + getLast4BiWeeks() + "'" +
            ", last12Weeks='" + getLast12Weeks() + "'" +
            ", last52Weeks='" + getLast52Weeks() + "'" +
            "}";
    }
}
