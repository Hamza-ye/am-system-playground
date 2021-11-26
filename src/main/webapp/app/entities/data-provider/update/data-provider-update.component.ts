import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDataProvider, DataProvider } from '../data-provider.model';
import { DataProviderService } from '../service/data-provider.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

@Component({
  selector: 'app-data-provider-update',
  templateUrl: './data-provider-update.component.html',
})
export class DataProviderUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  familiesSharedCollection: IFamily[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    description: [],
    created: [],
    lastUpdated: [],
    mobile: [],
    createdBy: [],
    lastUpdatedBy: [],
    family: [],
  });

  constructor(
    protected dataProviderService: DataProviderService,
    protected userService: UserService,
    protected familyService: FamilyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataProvider }) => {
      if (dataProvider.id === undefined) {
        const today = dayjs().startOf('day');
        dataProvider.created = today;
        dataProvider.lastUpdated = today;
      }

      this.updateForm(dataProvider);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dataProvider = this.createFromForm();
    if (dataProvider.id !== undefined) {
      this.subscribeToSaveResponse(this.dataProviderService.update(dataProvider));
    } else {
      this.subscribeToSaveResponse(this.dataProviderService.create(dataProvider));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFamilyById(index: number, item: IFamily): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataProvider>>): void {
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

  protected updateForm(dataProvider: IDataProvider): void {
    this.editForm.patchValue({
      id: dataProvider.id,
      uid: dataProvider.uid,
      code: dataProvider.code,
      name: dataProvider.name,
      description: dataProvider.description,
      created: dataProvider.created ? dataProvider.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: dataProvider.lastUpdated ? dataProvider.lastUpdated.format(DATE_TIME_FORMAT) : null,
      mobile: dataProvider.mobile,
      createdBy: dataProvider.createdBy,
      lastUpdatedBy: dataProvider.lastUpdatedBy,
      family: dataProvider.family,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      dataProvider.createdBy,
      dataProvider.lastUpdatedBy
    );
    this.familiesSharedCollection = this.familyService.addFamilyToCollectionIfMissing(this.familiesSharedCollection, dataProvider.family);
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

    this.familyService
      .query()
      .pipe(map((res: HttpResponse<IFamily[]>) => res.body ?? []))
      .pipe(map((families: IFamily[]) => this.familyService.addFamilyToCollectionIfMissing(families, this.editForm.get('family')!.value)))
      .subscribe((families: IFamily[]) => (this.familiesSharedCollection = families));
  }

  protected createFromForm(): IDataProvider {
    return {
      ...new DataProvider(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      mobile: this.editForm.get(['mobile'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      family: this.editForm.get(['family'])!.value,
    };
  }
}
