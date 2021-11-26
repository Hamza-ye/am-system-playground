import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { ITeam } from 'app/entities/team/team.model';
import { MovementType } from 'app/entities/enumerations/movement-type.model';

export interface IWhMovement {
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
  initiatedWh?: IWarehouse;
  theOtherSideWh?: IWarehouse | null;
  team?: ITeam | null;
}

export class WhMovement implements IWhMovement {
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
    public initiatedWh?: IWarehouse,
    public theOtherSideWh?: IWarehouse | null,
    public team?: ITeam | null
  ) {
    this.confirmedByOtherSide = this.confirmedByOtherSide ?? false;
  }
}

export function getWhMovementIdentifier(whMovement: IWhMovement): number | undefined {
  return whMovement.id;
}
