import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMalariaUnitStaffMember, MalariaUnitStaffMember } from '../malaria-unit-staff-member.model';
import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { MalariaUnitService } from 'app/entities/malaria-unit/service/malaria-unit.service';

@Component({
  selector: 'app-malaria-unit-staff-member-update',
  templateUrl: './malaria-unit-staff-member-update.component.html',
})
export class MalariaUnitStaffMemberUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peopleSharedCollection: IPerson[] = [];
  malariaUnitsSharedCollection: IMalariaUnit[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    description: [],
    created: [],
    lastUpdated: [],
    memberNo: [null, [Validators.required]],
    memberType: [null, [Validators.required]],
    user: [],
    lastUpdatedBy: [],
    person: [],
    malariaUnit: [null, Validators.required],
  });

  constructor(
    protected malariaUnitStaffMemberService: MalariaUnitStaffMemberService,
    protected userService: UserService,
    protected personService: PersonService,
    protected malariaUnitService: MalariaUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaUnitStaffMember }) => {
      if (malariaUnitStaffMember.id === undefined) {
        const today = dayjs().startOf('day');
        malariaUnitStaffMember.created = today;
        malariaUnitStaffMember.lastUpdated = today;
      }

      this.updateForm(malariaUnitStaffMember);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const malariaUnitStaffMember = this.createFromForm();
    if (malariaUnitStaffMember.id !== undefined) {
      this.subscribeToSaveResponse(this.malariaUnitStaffMemberService.update(malariaUnitStaffMember));
    } else {
      this.subscribeToSaveResponse(this.malariaUnitStaffMemberService.create(malariaUnitStaffMember));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackMalariaUnitById(index: number, item: IMalariaUnit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMalariaUnitStaffMember>>): void {
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

  protected updateForm(malariaUnitStaffMember: IMalariaUnitStaffMember): void {
    this.editForm.patchValue({
      id: malariaUnitStaffMember.id,
      uid: malariaUnitStaffMember.uid,
      code: malariaUnitStaffMember.code,
      description: malariaUnitStaffMember.description,
      created: malariaUnitStaffMember.created ? malariaUnitStaffMember.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: malariaUnitStaffMember.lastUpdated ? malariaUnitStaffMember.lastUpdated.format(DATE_TIME_FORMAT) : null,
      memberNo: malariaUnitStaffMember.memberNo,
      memberType: malariaUnitStaffMember.memberType,
      user: malariaUnitStaffMember.user,
      lastUpdatedBy: malariaUnitStaffMember.lastUpdatedBy,
      person: malariaUnitStaffMember.person,
      malariaUnit: malariaUnitStaffMember.malariaUnit,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      malariaUnitStaffMember.user,
      malariaUnitStaffMember.lastUpdatedBy
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(
      this.peopleSharedCollection,
      malariaUnitStaffMember.person
    );
    this.malariaUnitsSharedCollection = this.malariaUnitService.addMalariaUnitToCollectionIfMissing(
      this.malariaUnitsSharedCollection,
      malariaUnitStaffMember.malariaUnit
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

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.malariaUnitService
      .query()
      .pipe(map((res: HttpResponse<IMalariaUnit[]>) => res.body ?? []))
      .pipe(
        map((malariaUnits: IMalariaUnit[]) =>
          this.malariaUnitService.addMalariaUnitToCollectionIfMissing(malariaUnits, this.editForm.get('malariaUnit')!.value)
        )
      )
      .subscribe((malariaUnits: IMalariaUnit[]) => (this.malariaUnitsSharedCollection = malariaUnits));
  }

  protected createFromForm(): IMalariaUnitStaffMember {
    return {
      ...new MalariaUnitStaffMember(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      memberNo: this.editForm.get(['memberNo'])!.value,
      memberType: this.editForm.get(['memberType'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      person: this.editForm.get(['person'])!.value,
      malariaUnit: this.editForm.get(['malariaUnit'])!.value,
    };
  }
}
