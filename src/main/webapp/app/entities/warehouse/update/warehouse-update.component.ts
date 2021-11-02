import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWarehouse, Warehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IActivity } from 'app/entities/activity/activity.model';
import { ActivityService } from 'app/entities/activity/service/activity.service';

@Component({
  selector: 'app-warehouse-update',
  templateUrl: './warehouse-update.component.html',
})
export class WarehouseUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  activitiesSharedCollection: IActivity[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    whNo: [null, [Validators.required, Validators.min(1)]],
    initialBalancePlan: [null, [Validators.required, Validators.min(0)]],
    initialBalanceActual: [null, [Validators.required, Validators.min(0)]],
    user: [],
    lastUpdatedBy: [],
    activity: [null, Validators.required],
  });

  constructor(
    protected warehouseService: WarehouseService,
    protected userService: UserService,
    protected activityService: ActivityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warehouse }) => {
      if (warehouse.id === undefined) {
        const today = dayjs().startOf('day');
        warehouse.created = today;
        warehouse.lastUpdated = today;
      }

      this.updateForm(warehouse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warehouse = this.createFromForm();
    if (warehouse.id !== undefined) {
      this.subscribeToSaveResponse(this.warehouseService.update(warehouse));
    } else {
      this.subscribeToSaveResponse(this.warehouseService.create(warehouse));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackActivityById(index: number, item: IActivity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarehouse>>): void {
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

  protected updateForm(warehouse: IWarehouse): void {
    this.editForm.patchValue({
      id: warehouse.id,
      uid: warehouse.uid,
      code: warehouse.code,
      name: warehouse.name,
      created: warehouse.created ? warehouse.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: warehouse.lastUpdated ? warehouse.lastUpdated.format(DATE_TIME_FORMAT) : null,
      whNo: warehouse.whNo,
      initialBalancePlan: warehouse.initialBalancePlan,
      initialBalanceActual: warehouse.initialBalanceActual,
      user: warehouse.user,
      lastUpdatedBy: warehouse.lastUpdatedBy,
      activity: warehouse.activity,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      warehouse.user,
      warehouse.lastUpdatedBy
    );
    this.activitiesSharedCollection = this.activityService.addActivityToCollectionIfMissing(
      this.activitiesSharedCollection,
      warehouse.activity
    );
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

    this.activityService
      .query()
      .pipe(map((res: HttpResponse<IActivity[]>) => res.body ?? []))
      .pipe(
        map((activities: IActivity[]) =>
          this.activityService.addActivityToCollectionIfMissing(activities, this.editForm.get('activity')!.value)
        )
      )
      .subscribe((activities: IActivity[]) => (this.activitiesSharedCollection = activities));
  }

  protected createFromForm(): IWarehouse {
    return {
      ...new Warehouse(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      whNo: this.editForm.get(['whNo'])!.value,
      initialBalancePlan: this.editForm.get(['initialBalancePlan'])!.value,
      initialBalanceActual: this.editForm.get(['initialBalanceActual'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      activity: this.editForm.get(['activity'])!.value,
    };
  }
}
