import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { IPeriod } from 'app/entities/period/period.model';
import { IDataSet } from 'app/entities/data-set/data-set.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';

export interface IDengueCasesReport {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  rdtTested?: number | null;
  rdtPositive?: number | null;
  probableCases?: number | null;
  inpatientCases?: number | null;
  deathCases?: number | null;
  treated?: number | null;
  suspectedCases?: number | null;
  comment?: string | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  reportClass?: ICasesReportClass;
  period?: IPeriod;
  dataSet?: IDataSet | null;
  organisationUnit?: IOrganisationUnit | null;
}

export class DengueCasesReport implements IDengueCasesReport {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public rdtTested?: number | null,
    public rdtPositive?: number | null,
    public probableCases?: number | null,
    public inpatientCases?: number | null,
    public deathCases?: number | null,
    public treated?: number | null,
    public suspectedCases?: number | null,
    public comment?: string | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public reportClass?: ICasesReportClass,
    public period?: IPeriod,
    public dataSet?: IDataSet | null,
    public organisationUnit?: IOrganisationUnit | null
  ) {}
}

export function getDengueCasesReportIdentifier(dengueCasesReport: IDengueCasesReport): number | undefined {
  return dengueCasesReport.id;
}
