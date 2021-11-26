import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWhMovement, WhMovement } from '../wh-movement.model';
import { WhMovementService } from '../service/wh-movement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-wh-movement-update',
  templateUrl: './wh-movement-update.component.html',
})
export class WhMovementUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  warehousesSharedCollection: IWarehouse[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    movementType: [null, [Validators.required]],
    quantity: [null, [Validators.required, Validators.min(0)]],
    reconciliationSource: [],
    reconciliationDestination: [],
    confirmedByOtherSide: [],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    day: [null, Validators.required],
    initiatedWh: [null, Validators.required],
    theOtherSideWh: [],
    team: [],
  });

  constructor(
    protected whMovementService: WhMovementService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected warehouseService: WarehouseService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ whMovement }) => {
      this.updateForm(whMovement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const whMovement = this.createFromForm();
    if (whMovement.id !== undefined) {
      this.subscribeToSaveResponse(this.whMovementService.update(whMovement));
    } else {
      this.subscribeToSaveResponse(this.whMovementService.create(whMovement));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackWarehouseById(index: number, item: IWarehouse): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWhMovement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(whMovement: IWhMovement): void {
    this.editForm.patchValue({
      id: whMovement.id,
      movementType: whMovement.movementType,
      quantity: whMovement.quantity,
      reconciliationSource: whMovement.reconciliationSource,
      reconciliationDestination: whMovement.reconciliationDestination,
      confirmedByOtherSide: whMovement.confirmedByOtherSide,
      comment: whMovement.comment,
      createdBy: whMovement.createdBy,
      lastUpdatedBy: whMovement.lastUpdatedBy,
      day: whMovement.day,
      initiatedWh: whMovement.initiatedWh,
      theOtherSideWh: whMovement.theOtherSideWh,
      team: whMovement.team,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      whMovement.createdBy,
      whMovement.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      whMovement.day
    );
    this.warehousesSharedCollection = this.warehouseService.addWarehouseToCollectionIfMissing(
      this.warehousesSharedCollection,
      whMovement.initiatedWh,
      whMovement.theOtherSideWh
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, whMovement.team);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(
            users,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('lastUpdatedBy')!.value
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.workingDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkingDay[]>) => res.body ?? []))
      .pipe(
        map((workingDays: IWorkingDay[]) =>
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('day')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.warehouseService
      .query()
      .pipe(map((res: HttpResponse<IWarehouse[]>) => res.body ?? []))
      .pipe(
        map((warehouses: IWarehouse[]) =>
          this.warehouseService.addWarehouseToCollectionIfMissing(
            warehouses,
            this.editForm.get('initiatedWh')!.value,
            this.editForm.get('theOtherSideWh')!.value
          )
        )
      )
      .subscribe((warehouses: IWarehouse[]) => (this.warehousesSharedCollection = warehouses));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): IWhMovement {
    return {
      ...new WhMovement(),
      id: this.editForm.get(['id'])!.value,
      movementType: this.editForm.get(['movementType'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      reconciliationSource: this.editForm.get(['reconciliationSource'])!.value,
      reconciliationDestination: this.editForm.get(['reconciliationDestination'])!.value,
      confirmedByOtherSide: this.editForm.get(['confirmedByOtherSide'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      day: this.editForm.get(['day'])!.value,
      initiatedWh: this.editForm.get(['initiatedWh'])!.value,
      theOtherSideWh: this.editForm.get(['theOtherSideWh'])!.value,
      team: this.editForm.get(['team'])!.value,
    };
  }
}
