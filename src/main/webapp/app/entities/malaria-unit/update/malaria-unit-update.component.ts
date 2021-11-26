import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMalariaUnit, MalariaUnit } from '../malaria-unit.model';
import { MalariaUnitService } from '../service/malaria-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-malaria-unit-update',
  templateUrl: './malaria-unit-update.component.html',
})
export class MalariaUnitUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    shortName: [null, [Validators.maxLength(50)]],
    description: [],
    created: [],
    lastUpdated: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected malariaUnitService: MalariaUnitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaUnit }) => {
      if (malariaUnit.id === undefined) {
        const today = dayjs().startOf('day');
        malariaUnit.created = today;
        malariaUnit.lastUpdated = today;
      }

      this.updateForm(malariaUnit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const malariaUnit = this.createFromForm();
    if (malariaUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.malariaUnitService.update(malariaUnit));
    } else {
      this.subscribeToSaveResponse(this.malariaUnitService.create(malariaUnit));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMalariaUnit>>): void {
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

  protected updateForm(malariaUnit: IMalariaUnit): void {
    this.editForm.patchValue({
      id: malariaUnit.id,
      uid: malariaUnit.uid,
      code: malariaUnit.code,
      name: malariaUnit.name,
      shortName: malariaUnit.shortName,
      description: malariaUnit.description,
      created: malariaUnit.created ? malariaUnit.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: malariaUnit.lastUpdated ? malariaUnit.lastUpdated.format(DATE_TIME_FORMAT) : null,
      createdBy: malariaUnit.createdBy,
      lastUpdatedBy: malariaUnit.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      malariaUnit.createdBy,
      malariaUnit.lastUpdatedBy
    );
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
  }

  protected createFromForm(): IMalariaUnit {
    return {
      ...new MalariaUnit(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
