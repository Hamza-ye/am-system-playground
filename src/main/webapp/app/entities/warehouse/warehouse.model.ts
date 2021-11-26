import * as dayjs from 'dayjs';
import { IWHMovement } from 'app/entities/wh-movement/wh-movement.model';
import { IUser } from 'app/entities/user/user.model';
import { IActivity } from 'app/entities/activity/activity.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IWarehouse {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  whNo?: number;
  initialBalancePlan?: number;
  initialBalanceActual?: number;
  initiatedMovements?: IWHMovement[] | null;
  notInitiatedMovements?: IWHMovement[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  activity?: IActivity;
  teams?: ITeam[] | null;
}

export class Warehouse implements IWarehouse {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public whNo?: number,
    public initialBalancePlan?: number,
    public initialBalanceActual?: number,
    public initiatedMovements?: IWHMovement[] | null,
    public notInitiatedMovements?: IWHMovement[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public activity?: IActivity,
    public teams?: ITeam[] | null
  ) {}
}

export function getWarehouseIdentifier(warehouse: IWarehouse): number | undefined {
  return warehouse.id;
}
