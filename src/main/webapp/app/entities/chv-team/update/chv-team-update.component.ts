import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICHVTeam, CHVTeam } from '../chv-team.model';
import { CHVTeamService } from '../service/chv-team.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';

@Component({
  selector: 'app-chv-team-update',
  templateUrl: './chv-team-update.component.html',
})
export class CHVTeamUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peopleSharedCollection: IPerson[] = [];
  cHVSSharedCollection: ICHV[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    description: [],
    created: [],
    lastUpdated: [],
    teamNo: [null, [Validators.required, Validators.pattern('^[0-9]{1,12}')]],
    teamType: [null, [Validators.required]],
    user: [],
    lastUpdatedBy: [],
    person: [],
    responsibleForChvs: [],
  });

  constructor(
    protected cHVTeamService: CHVTeamService,
    protected userService: UserService,
    protected personService: PersonService,
    protected cHVService: CHVService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVTeam }) => {
      if (cHVTeam.id === undefined) {
        const today = dayjs().startOf('day');
        cHVTeam.created = today;
        cHVTeam.lastUpdated = today;
      }

      this.updateForm(cHVTeam);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cHVTeam = this.createFromForm();
    if (cHVTeam.id !== undefined) {
      this.subscribeToSaveResponse(this.cHVTeamService.update(cHVTeam));
    } else {
      this.subscribeToSaveResponse(this.cHVTeamService.create(cHVTeam));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackCHVById(index: number, item: ICHV): number {
    return item.id!;
  }

  getSelectedCHV(option: ICHV, selectedVals?: ICHV[]): ICHV {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICHVTeam>>): void {
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

  protected updateForm(cHVTeam: ICHVTeam): void {
    this.editForm.patchValue({
      id: cHVTeam.id,
      uid: cHVTeam.uid,
      code: cHVTeam.code,
      name: cHVTeam.name,
      description: cHVTeam.description,
      created: cHVTeam.created ? cHVTeam.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: cHVTeam.lastUpdated ? cHVTeam.lastUpdated.format(DATE_TIME_FORMAT) : null,
      teamNo: cHVTeam.teamNo,
      teamType: cHVTeam.teamType,
      user: cHVTeam.user,
      lastUpdatedBy: cHVTeam.lastUpdatedBy,
      person: cHVTeam.person,
      responsibleForChvs: cHVTeam.responsibleForChvs,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      cHVTeam.user,
      cHVTeam.lastUpdatedBy
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, cHVTeam.person);
    this.cHVSSharedCollection = this.cHVService.addCHVToCollectionIfMissing(
      this.cHVSSharedCollection,
      ...(cHVTeam.responsibleForChvs ?? [])
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

    this.cHVService
      .query()
      .pipe(map((res: HttpResponse<ICHV[]>) => res.body ?? []))
      .pipe(
        map((cHVS: ICHV[]) => this.cHVService.addCHVToCollectionIfMissing(cHVS, ...(this.editForm.get('responsibleForChvs')!.value ?? [])))
      )
      .subscribe((cHVS: ICHV[]) => (this.cHVSSharedCollection = cHVS));
  }

  protected createFromForm(): ICHVTeam {
    return {
      ...new CHVTeam(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      teamNo: this.editForm.get(['teamNo'])!.value,
      teamType: this.editForm.get(['teamType'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      person: this.editForm.get(['person'])!.value,
      responsibleForChvs: this.editForm.get(['responsibleForChvs'])!.value,
    };
  }
}
