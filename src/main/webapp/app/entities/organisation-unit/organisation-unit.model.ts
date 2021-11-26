import * as dayjs from 'dayjs';
import { IMalariaCasesReport } from 'app/entities/malaria-cases-report/malaria-cases-report.model';
import { IDengueCasesReport } from 'app/entities/dengue-cases-report/dengue-cases-report.model';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { IUser } from 'app/entities/user/user.model';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { IChv } from 'app/entities/chv/chv.model';
import { IDemographicData } from 'app/entities/demographic-data/demographic-data.model';
import { IOrganisationUnitGroup } from 'app/entities/organisation-unit-group/organisation-unit-group.model';
import { IPerson } from 'app/entities/person/person.model';
import { IDataSet } from 'app/entities/data-set/data-set.model';
import { OrganisationUnitType } from 'app/entities/enumerations/organisation-unit-type.model';

export interface IOrganisationUnit {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  shortName?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  path?: string | null;
  hierarchyLevel?: number | null;
  openingDate?: dayjs.Dayjs | null;
  comment?: string | null;
  closedDate?: dayjs.Dayjs | null;
  url?: string | null;
  contactPerson?: string | null;
  address?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  organisationUnitType?: OrganisationUnitType;
  malariaReports?: IMalariaCasesReport[] | null;
  dengueReports?: IDengueCasesReport[] | null;
  image?: IFileResource | null;
  parent?: IOrganisationUnit | null;
  hfHomeSubVillage?: IOrganisationUnit | null;
  coveredByHf?: IOrganisationUnit | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  malariaUnit?: IMalariaUnit | null;
  assignedChv?: IChv | null;
  children?: IOrganisationUnit[] | null;
  demographicData?: IDemographicData[] | null;
  groups?: IOrganisationUnitGroup[] | null;
  people?: IPerson[] | null;
  dataViewPeople?: IPerson[] | null;
  dataSets?: IDataSet[] | null;
}

export class OrganisationUnit implements IOrganisationUnit {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public shortName?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public path?: string | null,
    public hierarchyLevel?: number | null,
    public openingDate?: dayjs.Dayjs | null,
    public comment?: string | null,
    public closedDate?: dayjs.Dayjs | null,
    public url?: string | null,
    public contactPerson?: string | null,
    public address?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public organisationUnitType?: OrganisationUnitType,
    public malariaReports?: IMalariaCasesReport[] | null,
    public dengueReports?: IDengueCasesReport[] | null,
    public image?: IFileResource | null,
    public parent?: IOrganisationUnit | null,
    public hfHomeSubVillage?: IOrganisationUnit | null,
    public coveredByHf?: IOrganisationUnit | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public malariaUnit?: IMalariaUnit | null,
    public assignedChv?: IChv | null,
    public children?: IOrganisationUnit[] | null,
    public demographicData?: IDemographicData[] | null,
    public groups?: IOrganisationUnitGroup[] | null,
    public people?: IPerson[] | null,
    public dataViewPeople?: IPerson[] | null,
    public dataSets?: IDataSet[] | null
  ) {}
}

export function getOrganisationUnitIdentifier(organisationUnit: IOrganisationUnit): number | undefined {
  return organisationUnit.id;
}
