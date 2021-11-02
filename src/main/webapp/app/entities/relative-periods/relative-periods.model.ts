export interface IRelativePeriods {
  id?: number;
  thisDay?: boolean | null;
  yesterday?: boolean | null;
  last3Days?: boolean | null;
  last7Days?: boolean | null;
  last14Days?: boolean | null;
  thisMonth?: boolean | null;
  lastMonth?: boolean | null;
  thisBimonth?: boolean | null;
  lastBimonth?: boolean | null;
  thisQuarter?: boolean | null;
  lastQuarter?: boolean | null;
  thisSixMonth?: boolean | null;
  lastSixMonth?: boolean | null;
  weeksThisYear?: boolean | null;
  monthsThisYear?: boolean | null;
  biMonthsThisYear?: boolean | null;
  quartersThisYear?: boolean | null;
  thisYear?: boolean | null;
  monthsLastYear?: boolean | null;
  quartersLastYear?: boolean | null;
  lastYear?: boolean | null;
  last5Years?: boolean | null;
  last12Months?: boolean | null;
  last6Months?: boolean | null;
  last3Months?: boolean | null;
  last6BiMonths?: boolean | null;
  last4Quarters?: boolean | null;
  last2SixMonths?: boolean | null;
  thisFinancialYear?: boolean | null;
  lastFinancialYear?: boolean | null;
  last5FinancialYears?: boolean | null;
  thisWeek?: boolean | null;
  lastWeek?: boolean | null;
  thisBiWeek?: boolean | null;
  lastBiWeek?: boolean | null;
  last4Weeks?: boolean | null;
  last4BiWeeks?: boolean | null;
  last12Weeks?: boolean | null;
  last52Weeks?: boolean | null;
}

export class RelativePeriods implements IRelativePeriods {
  constructor(
    public id?: number,
    public thisDay?: boolean | null,
    public yesterday?: boolean | null,
    public last3Days?: boolean | null,
    public last7Days?: boolean | null,
    public last14Days?: boolean | null,
    public thisMonth?: boolean | null,
    public lastMonth?: boolean | null,
    public thisBimonth?: boolean | null,
    public lastBimonth?: boolean | null,
    public thisQuarter?: boolean | null,
    public lastQuarter?: boolean | null,
    public thisSixMonth?: boolean | null,
    public lastSixMonth?: boolean | null,
    public weeksThisYear?: boolean | null,
    public monthsThisYear?: boolean | null,
    public biMonthsThisYear?: boolean | null,
    public quartersThisYear?: boolean | null,
    public thisYear?: boolean | null,
    public monthsLastYear?: boolean | null,
    public quartersLastYear?: boolean | null,
    public lastYear?: boolean | null,
    public last5Years?: boolean | null,
    public last12Months?: boolean | null,
    public last6Months?: boolean | null,
    public last3Months?: boolean | null,
    public last6BiMonths?: boolean | null,
    public last4Quarters?: boolean | null,
    public last2SixMonths?: boolean | null,
    public thisFinancialYear?: boolean | null,
    public lastFinancialYear?: boolean | null,
    public last5FinancialYears?: boolean | null,
    public thisWeek?: boolean | null,
    public lastWeek?: boolean | null,
    public thisBiWeek?: boolean | null,
    public lastBiWeek?: boolean | null,
    public last4Weeks?: boolean | null,
    public last4BiWeeks?: boolean | null,
    public last12Weeks?: boolean | null,
    public last52Weeks?: boolean | null
  ) {
    this.thisDay = this.thisDay ?? false;
    this.yesterday = this.yesterday ?? false;
    this.last3Days = this.last3Days ?? false;
    this.last7Days = this.last7Days ?? false;
    this.last14Days = this.last14Days ?? false;
    this.thisMonth = this.thisMonth ?? false;
    this.lastMonth = this.lastMonth ?? false;
    this.thisBimonth = this.thisBimonth ?? false;
    this.lastBimonth = this.lastBimonth ?? false;
    this.thisQuarter = this.thisQuarter ?? false;
    this.lastQuarter = this.lastQuarter ?? false;
    this.thisSixMonth = this.thisSixMonth ?? false;
    this.lastSixMonth = this.lastSixMonth ?? false;
    this.weeksThisYear = this.weeksThisYear ?? false;
    this.monthsThisYear = this.monthsThisYear ?? false;
    this.biMonthsThisYear = this.biMonthsThisYear ?? false;
    this.quartersThisYear = this.quartersThisYear ?? false;
    this.thisYear = this.thisYear ?? false;
    this.monthsLastYear = this.monthsLastYear ?? false;
    this.quartersLastYear = this.quartersLastYear ?? false;
    this.lastYear = this.lastYear ?? false;
    this.last5Years = this.last5Years ?? false;
    this.last12Months = this.last12Months ?? false;
    this.last6Months = this.last6Months ?? false;
    this.last3Months = this.last3Months ?? false;
    this.last6BiMonths = this.last6BiMonths ?? false;
    this.last4Quarters = this.last4Quarters ?? false;
    this.last2SixMonths = this.last2SixMonths ?? false;
    this.thisFinancialYear = this.thisFinancialYear ?? false;
    this.lastFinancialYear = this.lastFinancialYear ?? false;
    this.last5FinancialYears = this.last5FinancialYears ?? false;
    this.thisWeek = this.thisWeek ?? false;
    this.lastWeek = this.lastWeek ?? false;
    this.thisBiWeek = this.thisBiWeek ?? false;
    this.lastBiWeek = this.lastBiWeek ?? false;
    this.last4Weeks = this.last4Weeks ?? false;
    this.last4BiWeeks = this.last4BiWeeks ?? false;
    this.last12Weeks = this.last12Weeks ?? false;
    this.last52Weeks = this.last52Weeks ?? false;
  }
}

export function getRelativePeriodsIdentifier(relativePeriods: IRelativePeriods): number | undefined {
  return relativePeriods.id;
}
