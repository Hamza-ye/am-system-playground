import * as dayjs from 'dayjs';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IUser } from 'app/entities/user/user.model';
import { ICHV } from 'app/entities/chv/chv.model';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MalariaTestResult } from 'app/entities/enumerations/malaria-test-result.model';

export interface ICHVMalariaCaseReport {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs | null;
  individualName?: string | null;
  gender?: Gender | null;
  isPregnant?: boolean | null;
  malariaTestResult?: MalariaTestResult | null;
  drugsGiven?: number | null;
  suppsGiven?: number | null;
  referral?: boolean | null;
  barImageUrl?: string | null;
  comment?: string | null;
  subVillage?: IOrganisationUnit | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  chv?: ICHV;
  reportClass?: ICasesReportClass;
}

export class CHVMalariaCaseReport implements ICHVMalariaCaseReport {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public date?: dayjs.Dayjs | null,
    public individualName?: string | null,
    public gender?: Gender | null,
    public isPregnant?: boolean | null,
    public malariaTestResult?: MalariaTestResult | null,
    public drugsGiven?: number | null,
    public suppsGiven?: number | null,
    public referral?: boolean | null,
    public barImageUrl?: string | null,
    public comment?: string | null,
    public subVillage?: IOrganisationUnit | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public chv?: ICHV,
    public reportClass?: ICasesReportClass
  ) {
    this.isPregnant = this.isPregnant ?? false;
    this.referral = this.referral ?? false;
  }
}

export function getCHVMalariaCaseReportIdentifier(cHVMalariaCaseReport: ICHVMalariaCaseReport): number | undefined {
  return cHVMalariaCaseReport.id;
}
