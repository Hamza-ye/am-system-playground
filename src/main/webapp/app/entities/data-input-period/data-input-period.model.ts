import * as dayjs from 'dayjs';
import { IPeriod } from 'app/entities/period/period.model';

export interface IDataInputPeriod {
  id?: number;
  openingDate?: dayjs.Dayjs | null;
  closingDate?: dayjs.Dayjs | null;
  period?: IPeriod | null;
}

export class DataInputPeriod implements IDataInputPeriod {
  constructor(
    public id?: number,
    public openingDate?: dayjs.Dayjs | null,
    public closingDate?: dayjs.Dayjs | null,
    public period?: IPeriod | null
  ) {}
}

export function getDataInputPeriodIdentifier(dataInputPeriod: IDataInputPeriod): number | undefined {
  return dataInputPeriod.id;
}
