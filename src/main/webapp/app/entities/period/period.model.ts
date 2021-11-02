import * as dayjs from 'dayjs';
import { IPeriodType } from 'app/entities/period-type/period-type.model';

export interface IPeriod {
  id?: number;
  name?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  periodType?: IPeriodType;
}

export class Period implements IPeriod {
  constructor(
    public id?: number,
    public name?: string | null,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public periodType?: IPeriodType
  ) {}
}

export function getPeriodIdentifier(period: IPeriod): number | undefined {
  return period.id;
}
