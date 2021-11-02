export interface IPeriodType {
  id?: number;
  name?: string | null;
}

export class PeriodType implements IPeriodType {
  constructor(public id?: number, public name?: string | null) {}
}

export function getPeriodTypeIdentifier(periodType: IPeriodType): number | undefined {
  return periodType.id;
}
