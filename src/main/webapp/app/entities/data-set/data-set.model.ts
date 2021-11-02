import * as dayjs from 'dayjs';
import { IMalariaCasesReport } from 'app/entities/malaria-cases-report/malaria-cases-report.model';
import { IDengueCasesReport } from 'app/entities/dengue-cases-report/dengue-cases-report.model';
import { IPeriodType } from 'app/entities/period-type/period-type.model';
import { IPeopleGroup } from 'app/entities/people-group/people-group.model';
import { IUser } from 'app/entities/user/user.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';

export interface IDataSet {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  shortName?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  expiryDays?: number | null;
  timelyDays?: number | null;
  malariaCasesReports?: IMalariaCasesReport[] | null;
  dengueCasesReports?: IDengueCasesReport[] | null;
  periodType?: IPeriodType | null;
  notificationRecipients?: IPeopleGroup | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  sources?: IOrganisationUnit[] | null;
}

export class DataSet implements IDataSet {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public shortName?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public expiryDays?: number | null,
    public timelyDays?: number | null,
    public malariaCasesReports?: IMalariaCasesReport[] | null,
    public dengueCasesReports?: IDengueCasesReport[] | null,
    public periodType?: IPeriodType | null,
    public notificationRecipients?: IPeopleGroup | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public sources?: IOrganisationUnit[] | null
  ) {}
}

export function getDataSetIdentifier(dataSet: IDataSet): number | undefined {
  return dataSet.id;
}
