import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { ITeam } from 'app/entities/team/team.model';
import { MovementType } from 'app/entities/enumerations/movement-type.model';

export interface IWHMovement {
  id?: number;
  movementType?: MovementType;
  quantity?: number;
  reconciliationSource?: string | null;
  reconciliationDestination?: string | null;
  confirmedByOtherSide?: boolean | null;
  comment?: string | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  day?: IWorkingDay;
  initiatedWH?: IWarehouse;
  theOtherSideWH?: IWarehouse | null;
  team?: ITeam | null;
}

export class WHMovement implements IWHMovement {
  constructor(
    public id?: number,
    public movementType?: MovementType,
    public quantity?: number,
    public reconciliationSource?: string | null,
    public reconciliationDestination?: string | null,
    public confirmedByOtherSide?: boolean | null,
    public comment?: string | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public day?: IWorkingDay,
    public initiatedWH?: IWarehouse,
    public theOtherSideWH?: IWarehouse | null,
    public team?: ITeam | null
  ) {
    this.confirmedByOtherSide = this.confirmedByOtherSide ?? false;
  }
}

export function getWHMovementIdentifier(wHMovement: IWHMovement): number | undefined {
  return wHMovement.id;
}
