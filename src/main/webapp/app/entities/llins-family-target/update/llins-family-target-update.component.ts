import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILlinsFamilyTarget, LlinsFamilyTarget } from '../llins-family-target.model';
import { LlinsFamilyTargetService } from '../service/llins-family-target.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-family-target-update',
  templateUrl: './llins-family-target-update.component.html',
})
export class LlinsFamilyTargetUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  familiesSharedCollection: IFamily[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    residentsIndividualsPlanned: [null, [Validators.required, Validators.min(0)]],
    idpsIndividualsPlanned: [null, [Validators.required, Validators.min(0)]],
    quantityPlanned: [null, [Validators.required, Validators.min(0)]],
    familyType: [null, [Validators.required]],
    statusOfFamilyTarget: [null, [Validators.required]],
    createdBy: [],
    lastUpdatedBy: [],
    dayPlanned: [null, Validators.required],
    family: [null, Validators.required],
    teamAssigned: [null, Validators.required],
  });

  constructor(
    protected llinsFamilyTargetService: LlinsFamilyTargetService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected familyService: FamilyService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyTarget }) => {
      if (llinsFamilyTarget.id === undefined) {
        const today = dayjs().startOf('day');
        llinsFamilyTarget.created = today;
        llinsFamilyTarget.lastUpdated = today;
      }

      this.updateForm(llinsFamilyTarget);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const llinsFamilyTarget = this.createFromForm();
    if (llinsFamilyTarget.id !== undefined) {
      this.subscribeToSaveResponse(this.llinsFamilyTargetService.update(llinsFamilyTarget));
    } else {
      this.subscribeToSaveResponse(this.llinsFamilyTargetService.create(llinsFamilyTarget));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackFamilyById(index: number, item: IFamily): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILlinsFamilyTarget>>): void {
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

  protected updateForm(llinsFamilyTarget: ILlinsFamilyTarget): void {
    this.editForm.patchValue({
      id: llinsFamilyTarget.id,
      uid: llinsFamilyTarget.uid,
      created: llinsFamilyTarget.created ? llinsFamilyTarget.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: llinsFamilyTarget.lastUpdated ? llinsFamilyTarget.lastUpdated.format(DATE_TIME_FORMAT) : null,
      residentsIndividualsPlanned: llinsFamilyTarget.residentsIndividualsPlanned,
      idpsIndividualsPlanned: llinsFamilyTarget.idpsIndividualsPlanned,
      quantityPlanned: llinsFamilyTarget.quantityPlanned,
      familyType: llinsFamilyTarget.familyType,
      statusOfFamilyTarget: llinsFamilyTarget.statusOfFamilyTarget,
      createdBy: llinsFamilyTarget.createdBy,
      lastUpdatedBy: llinsFamilyTarget.lastUpdatedBy,
      dayPlanned: llinsFamilyTarget.dayPlanned,
      family: llinsFamilyTarget.family,
      teamAssigned: llinsFamilyTarget.teamAssigned,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      llinsFamilyTarget.createdBy,
      llinsFamilyTarget.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      llinsFamilyTarget.dayPlanned
    );
    this.familiesSharedCollection = this.familyService.addFamilyToCollectionIfMissing(
      this.familiesSharedCollection,
      llinsFamilyTarget.family
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, llinsFamilyTarget.teamAssigned);
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
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayPlanned')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.familyService
      .query()
      .pipe(map((res: HttpResponse<IFamily[]>) => res.body ?? []))
      .pipe(map((families: IFamily[]) => this.familyService.addFamilyToCollectionIfMissing(families, this.editForm.get('family')!.value)))
      .subscribe((families: IFamily[]) => (this.familiesSharedCollection = families));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('teamAssigned')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILlinsFamilyTarget {
    return {
      ...new LlinsFamilyTarget(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      residentsIndividualsPlanned: this.editForm.get(['residentsIndividualsPlanned'])!.value,
      idpsIndividualsPlanned: this.editForm.get(['idpsIndividualsPlanned'])!.value,
      quantityPlanned: this.editForm.get(['quantityPlanned'])!.value,
      familyType: this.editForm.get(['familyType'])!.value,
      statusOfFamilyTarget: this.editForm.get(['statusOfFamilyTarget'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayPlanned: this.editForm.get(['dayPlanned'])!.value,
      family: this.editForm.get(['family'])!.value,
      teamAssigned: this.editForm.get(['teamAssigned'])!.value,
    };
  }
}
