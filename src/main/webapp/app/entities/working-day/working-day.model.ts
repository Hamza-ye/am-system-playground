export interface IWorkingDay {
  id?: number;
  dayNo?: number;
  dayLabel?: string;
}

export class WorkingDay implements IWorkingDay {
  constructor(public id?: number, public dayNo?: number, public dayLabel?: string) {}
}

export function getWorkingDayIdentifier(workingDay: IWorkingDay): number | undefined {
  return workingDay.id;
}
