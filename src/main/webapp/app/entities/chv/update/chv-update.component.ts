import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICHV, CHV } from '../chv.model';
import { CHVService } from '../service/chv.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-chv-update',
  templateUrl: './chv-update.component.html',
})
export class CHVUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPerson[] = [];
  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    description: [],
    created: [],
    lastUpdated: [],
    mobile: [],
    person: [],
    district: [null, Validators.required],
    homeSubvillage: [],
    managedByHf: [],
    user: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected cHVService: CHVService,
    protected personService: PersonService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHV }) => {
      if (cHV.id === undefined) {
        const today = dayjs().startOf('day');
        cHV.created = today;
        cHV.lastUpdated = today;
      }

      this.updateForm(cHV);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cHV = this.createFromForm();
    if (cHV.id !== undefined) {
      this.subscribeToSaveResponse(this.cHVService.update(cHV));
    } else {
      this.subscribeToSaveResponse(this.cHVService.create(cHV));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICHV>>): void {
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

  protected updateForm(cHV: ICHV): void {
    this.editForm.patchValue({
      id: cHV.id,
      uid: cHV.uid,
      code: cHV.code,
      description: cHV.description,
      created: cHV.created ? cHV.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: cHV.lastUpdated ? cHV.lastUpdated.format(DATE_TIME_FORMAT) : null,
      mobile: cHV.mobile,
      person: cHV.person,
      district: cHV.district,
      homeSubvillage: cHV.homeSubvillage,
      managedByHf: cHV.managedByHf,
      user: cHV.user,
      lastUpdatedBy: cHV.lastUpdatedBy,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, cHV.person);
    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      cHV.district,
      cHV.homeSubvillage,
      cHV.managedByHf
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, cHV.user, cHV.lastUpdatedBy);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'chv-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));

    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
            organisationUnits,
            this.editForm.get('district')!.value,
            this.editForm.get('homeSubvillage')!.value,
            this.editForm.get('managedByHf')!.value
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

  protected createFromForm(): ICHV {
    return {
      ...new CHV(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      mobile: this.editForm.get(['mobile'])!.value,
      person: this.editForm.get(['person'])!.value,
      district: this.editForm.get(['district'])!.value,
      homeSubvillage: this.editForm.get(['homeSubvillage'])!.value,
      managedByHf: this.editForm.get(['managedByHf'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
