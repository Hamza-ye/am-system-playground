import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFamily, Family } from '../family.model';
import { FamilyService } from '../service/family.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-family-update',
  templateUrl: './family-update.component.html',
})
export class FamilyUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    familyNo: [null, [Validators.required]],
    address: [],
    organisationUnit: [null, Validators.required],
    user: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected familyService: FamilyService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ family }) => {
      if (family.id === undefined) {
        const today = dayjs().startOf('day');
        family.created = today;
        family.lastUpdated = today;
      }

      this.updateForm(family);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const family = this.createFromForm();
    if (family.id !== undefined) {
      this.subscribeToSaveResponse(this.familyService.update(family));
    } else {
      this.subscribeToSaveResponse(this.familyService.create(family));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamily>>): void {
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

  protected updateForm(family: IFamily): void {
    this.editForm.patchValue({
      id: family.id,
      uid: family.uid,
      code: family.code,
      name: family.name,
      created: family.created ? family.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: family.lastUpdated ? family.lastUpdated.format(DATE_TIME_FORMAT) : null,
      familyNo: family.familyNo,
      address: family.address,
      organisationUnit: family.organisationUnit,
      user: family.user,
      lastUpdatedBy: family.lastUpdatedBy,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      family.organisationUnit
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      family.user,
      family.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
            organisationUnits,
            this.editForm.get('organisationUnit')!.value
          )
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('lastUpdatedBy')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IFamily {
    return {
      ...new Family(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      familyNo: this.editForm.get(['familyNo'])!.value,
      address: this.editForm.get(['address'])!.value,
      organisationUnit: this.editForm.get(['organisationUnit'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
