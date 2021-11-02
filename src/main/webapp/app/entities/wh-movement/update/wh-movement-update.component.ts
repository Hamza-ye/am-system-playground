import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWHMovement, WHMovement } from '../wh-movement.model';
import { WHMovementService } from '../service/wh-movement.service';
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
export class WHMovementUpdateComponent implements OnInit {
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
    user: [],
    lastUpdatedBy: [],
    day: [null, Validators.required],
    initiatedWH: [null, Validators.required],
    theOtherSideWH: [],
    team: [],
  });

  constructor(
    protected wHMovementService: WHMovementService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected warehouseService: WarehouseService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHMovement }) => {
      this.updateForm(wHMovement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wHMovement = this.createFromForm();
    if (wHMovement.id !== undefined) {
      this.subscribeToSaveResponse(this.wHMovementService.update(wHMovement));
    } else {
      this.subscribeToSaveResponse(this.wHMovementService.create(wHMovement));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWHMovement>>): void {
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

  protected updateForm(wHMovement: IWHMovement): void {
    this.editForm.patchValue({
      id: wHMovement.id,
      movementType: wHMovement.movementType,
      quantity: wHMovement.quantity,
      reconciliationSource: wHMovement.reconciliationSource,
      reconciliationDestination: wHMovement.reconciliationDestination,
      confirmedByOtherSide: wHMovement.confirmedByOtherSide,
      comment: wHMovement.comment,
      user: wHMovement.user,
      lastUpdatedBy: wHMovement.lastUpdatedBy,
      day: wHMovement.day,
      initiatedWH: wHMovement.initiatedWH,
      theOtherSideWH: wHMovement.theOtherSideWH,
      team: wHMovement.team,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      wHMovement.user,
      wHMovement.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      wHMovement.day
    );
    this.warehousesSharedCollection = this.warehouseService.addWarehouseToCollectionIfMissing(
      this.warehousesSharedCollection,
      wHMovement.initiatedWH,
      wHMovement.theOtherSideWH
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, wHMovement.team);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('lastUpdatedBy')!.value)
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
            this.editForm.get('initiatedWH')!.value,
            this.editForm.get('theOtherSideWH')!.value
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

  protected createFromForm(): IWHMovement {
    return {
      ...new WHMovement(),
      id: this.editForm.get(['id'])!.value,
      movementType: this.editForm.get(['movementType'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      reconciliationSource: this.editForm.get(['reconciliationSource'])!.value,
      reconciliationDestination: this.editForm.get(['reconciliationDestination'])!.value,
      confirmedByOtherSide: this.editForm.get(['confirmedByOtherSide'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      day: this.editForm.get(['day'])!.value,
      initiatedWH: this.editForm.get(['initiatedWH'])!.value,
      theOtherSideWH: this.editForm.get(['theOtherSideWH'])!.value,
      team: this.editForm.get(['team'])!.value,
    };
  }
}
