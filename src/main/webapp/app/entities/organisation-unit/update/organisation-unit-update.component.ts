import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrganisationUnit, OrganisationUnit } from '../organisation-unit.model';
import { OrganisationUnitService } from '../service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { MalariaUnitService } from 'app/entities/malaria-unit/service/malaria-unit.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';

@Component({
  selector: 'app-organisation-unit-update',
  templateUrl: './organisation-unit-update.component.html',
})
export class OrganisationUnitUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];
  malariaUnitsSharedCollection: IMalariaUnit[] = [];
  cHVSSharedCollection: ICHV[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    shortName: [null, [Validators.maxLength(50)]],
    created: [],
    lastUpdated: [],
    path: [],
    hierarchyLevel: [],
    openingDate: [],
    comment: [],
    closedDate: [],
    url: [],
    contactPerson: [],
    address: [],
    email: [],
    phoneNumber: [],
    organisationUnitType: [null, [Validators.required]],
    parent: [],
    hfHomeSubVillage: [],
    coveredByHf: [],
    user: [],
    lastUpdatedBy: [],
    malariaUnit: [],
    assignedChv: [],
  });

  constructor(
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected malariaUnitService: MalariaUnitService,
    protected cHVService: CHVService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organisationUnit }) => {
      if (organisationUnit.id === undefined) {
        const today = dayjs().startOf('day');
        organisationUnit.created = today;
        organisationUnit.lastUpdated = today;
      }

      this.updateForm(organisationUnit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organisationUnit = this.createFromForm();
    if (organisationUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.organisationUnitService.update(organisationUnit));
    } else {
      this.subscribeToSaveResponse(this.organisationUnitService.create(organisationUnit));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackMalariaUnitById(index: number, item: IMalariaUnit): number {
    return item.id!;
  }

  trackCHVById(index: number, item: ICHV): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisationUnit>>): void {
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

  protected updateForm(organisationUnit: IOrganisationUnit): void {
    this.editForm.patchValue({
      id: organisationUnit.id,
      uid: organisationUnit.uid,
      code: organisationUnit.code,
      name: organisationUnit.name,
      shortName: organisationUnit.shortName,
      created: organisationUnit.created ? organisationUnit.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: organisationUnit.lastUpdated ? organisationUnit.lastUpdated.format(DATE_TIME_FORMAT) : null,
      path: organisationUnit.path,
      hierarchyLevel: organisationUnit.hierarchyLevel,
      openingDate: organisationUnit.openingDate,
      comment: organisationUnit.comment,
      closedDate: organisationUnit.closedDate,
      url: organisationUnit.url,
      contactPerson: organisationUnit.contactPerson,
      address: organisationUnit.address,
      email: organisationUnit.email,
      phoneNumber: organisationUnit.phoneNumber,
      organisationUnitType: organisationUnit.organisationUnitType,
      parent: organisationUnit.parent,
      hfHomeSubVillage: organisationUnit.hfHomeSubVillage,
      coveredByHf: organisationUnit.coveredByHf,
      user: organisationUnit.user,
      lastUpdatedBy: organisationUnit.lastUpdatedBy,
      malariaUnit: organisationUnit.malariaUnit,
      assignedChv: organisationUnit.assignedChv,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      organisationUnit.parent,
      organisationUnit.hfHomeSubVillage,
      organisationUnit.coveredByHf
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      organisationUnit.user,
      organisationUnit.lastUpdatedBy
    );
    this.malariaUnitsSharedCollection = this.malariaUnitService.addMalariaUnitToCollectionIfMissing(
      this.malariaUnitsSharedCollection,
      organisationUnit.malariaUnit
    );
    this.cHVSSharedCollection = this.cHVService.addCHVToCollectionIfMissing(this.cHVSSharedCollection, organisationUnit.assignedChv);
  }

  protected loadRelationshipsOptions(): void {
    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
            organisationUnits,
            this.editForm.get('parent')!.value,
            this.editForm.get('hfHomeSubVillage')!.value,
            this.editForm.get('coveredByHf')!.value
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

    this.malariaUnitService
      .query()
      .pipe(map((res: HttpResponse<IMalariaUnit[]>) => res.body ?? []))
      .pipe(
        map((malariaUnits: IMalariaUnit[]) =>
          this.malariaUnitService.addMalariaUnitToCollectionIfMissing(malariaUnits, this.editForm.get('malariaUnit')!.value)
        )
      )
      .subscribe((malariaUnits: IMalariaUnit[]) => (this.malariaUnitsSharedCollection = malariaUnits));

    this.cHVService
      .query()
      .pipe(map((res: HttpResponse<ICHV[]>) => res.body ?? []))
      .pipe(map((cHVS: ICHV[]) => this.cHVService.addCHVToCollectionIfMissing(cHVS, this.editForm.get('assignedChv')!.value)))
      .subscribe((cHVS: ICHV[]) => (this.cHVSSharedCollection = cHVS));
  }

  protected createFromForm(): IOrganisationUnit {
    return {
      ...new OrganisationUnit(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      path: this.editForm.get(['path'])!.value,
      hierarchyLevel: this.editForm.get(['hierarchyLevel'])!.value,
      openingDate: this.editForm.get(['openingDate'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      closedDate: this.editForm.get(['closedDate'])!.value,
      url: this.editForm.get(['url'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      address: this.editForm.get(['address'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      organisationUnitType: this.editForm.get(['organisationUnitType'])!.value,
      parent: this.editForm.get(['parent'])!.value,
      hfHomeSubVillage: this.editForm.get(['hfHomeSubVillage'])!.value,
      coveredByHf: this.editForm.get(['coveredByHf'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      malariaUnit: this.editForm.get(['malariaUnit'])!.value,
      assignedChv: this.editForm.get(['assignedChv'])!.value,
    };
  }
}
