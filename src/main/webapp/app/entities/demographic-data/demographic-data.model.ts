import * as dayjs from 'dayjs';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IUser } from 'app/entities/user/user.model';
import { IDemographicDataSource } from 'app/entities/demographic-data-source/demographic-data-source.model';
import { DemographicDataLevel } from 'app/entities/enumerations/demographic-data-level.model';

export interface IDemographicData {
  id?: number;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs;
  level?: DemographicDataLevel | null;
  totalPopulation?: number | null;
  malePopulation?: number | null;
  femalePopulation?: number | null;
  lessThan5Population?: number | null;
  greaterThan5Population?: number | null;
  bw5And15Population?: number | null;
  greaterThan15Population?: number | null;
  household?: number | null;
  houses?: number | null;
  healthFacilities?: number | null;
  avgNoOfRooms?: number | null;
  avgRoomArea?: number | null;
  avgHouseArea?: number | null;
  individualsPerHousehold?: number | null;
  populationGrowthRate?: number | null;
  comment?: string | null;
  organisationUnit?: IOrganisationUnit | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  source?: IDemographicDataSource;
}

export class DemographicData implements IDemographicData {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public date?: dayjs.Dayjs,
    public level?: DemographicDataLevel | null,
    public totalPopulation?: number | null,
    public malePopulation?: number | null,
    public femalePopulation?: number | null,
    public lessThan5Population?: number | null,
    public greaterThan5Population?: number | null,
    public bw5And15Population?: number | null,
    public greaterThan15Population?: number | null,
    public household?: number | null,
    public houses?: number | null,
    public healthFacilities?: number | null,
    public avgNoOfRooms?: number | null,
    public avgRoomArea?: number | null,
    public avgHouseArea?: number | null,
    public individualsPerHousehold?: number | null,
    public populationGrowthRate?: number | null,
    public comment?: string | null,
    public organisationUnit?: IOrganisationUnit | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public source?: IDemographicDataSource
  ) {}
}

export function getDemographicDataIdentifier(demographicData: IDemographicData): number | undefined {
  return demographicData.id;
}
