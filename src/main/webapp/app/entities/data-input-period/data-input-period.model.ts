import * as dayjs from 'dayjs';
import { IPeriod } from 'app/entities/period/period.model';
import { IDataSet } from 'app/entities/data-set/data-set.model';

export interface IDataInputPeriod {
  id?: number;
  openingDate?: dayjs.Dayjs | null;
  closingDate?: dayjs.Dayjs | null;
  period?: IPeriod | null;
  dataSet?: IDataSet | null;
}

export class DataInputPeriod implements IDataInputPeriod {
  constructor(
    public id?: number,
    public openingDate?: dayjs.Dayjs | null,
    public closingDate?: dayjs.Dayjs | null,
    public period?: IPeriod | null,
    public dataSet?: IDataSet | null
  ) {}
}

export function getDataInputPeriodIdentifier(dataInputPeriod: IDataInputPeriod): number | undefined {
  return dataInputPeriod.id;
}
