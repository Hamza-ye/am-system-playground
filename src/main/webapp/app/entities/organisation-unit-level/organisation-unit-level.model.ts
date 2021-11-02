import * as dayjs from 'dayjs';

export interface IOrganisationUnitLevel {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  level?: number | null;
  offlineLevels?: number | null;
}

export class OrganisationUnitLevel implements IOrganisationUnitLevel {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public level?: number | null,
    public offlineLevels?: number | null
  ) {}
}

export function getOrganisationUnitLevelIdentifier(organisationUnitLevel: IOrganisationUnitLevel): number | undefined {
  return organisationUnitLevel.id;
}
