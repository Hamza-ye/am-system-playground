import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILlinsFamilyReport, LlinsFamilyReport } from '../llins-family-report.model';
import { LlinsFamilyReportService } from '../service/llins-family-report.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { LlinsFamilyTargetService } from 'app/entities/llins-family-target/service/llins-family-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-family-report-update',
  templateUrl: './llins-family-report-update.component.html',
})
export class LlinsFamilyReportUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  llinsFamilyTargetsSharedCollection: ILlinsFamilyTarget[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    checkNo: [],
    maleIndividuals: [null, [Validators.required, Validators.min(0)]],
    femaleIndividuals: [null, [Validators.required, Validators.min(0)]],
    lessThan5Males: [null, [Validators.required, Validators.min(0)]],
    lessThan5Females: [null, [Validators.required, Validators.min(0)]],
    pregnantWomen: [null, [Validators.required, Validators.min(0)]],
    quantityReceived: [null, [Validators.required, Validators.min(0)]],
    familyType: [null, [Validators.required]],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    targetDetails: [],
    executingTeam: [null, Validators.required],
  });

  constructor(
    protected llinsFamilyReportService: LlinsFamilyReportService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected llinsFamilyTargetService: LlinsFamilyTargetService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyReport }) => {
      if (llinsFamilyReport.id === undefined) {
        const today = dayjs().startOf('day');
        llinsFamilyReport.created = today;
        llinsFamilyReport.lastUpdated = today;
      }

      this.updateForm(llinsFamilyReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const llinsFamilyReport = this.createFromForm();
    if (llinsFamilyReport.id !== undefined) {
      this.subscribeToSaveResponse(this.llinsFamilyReportService.update(llinsFamilyReport));
    } else {
      this.subscribeToSaveResponse(this.llinsFamilyReportService.create(llinsFamilyReport));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLlinsFamilyTargetById(index: number, item: ILlinsFamilyTarget): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILlinsFamilyReport>>): void {
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

  protected updateForm(llinsFamilyReport: ILlinsFamilyReport): void {
    this.editForm.patchValue({
      id: llinsFamilyReport.id,
      uid: llinsFamilyReport.uid,
      created: llinsFamilyReport.created ? llinsFamilyReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: llinsFamilyReport.lastUpdated ? llinsFamilyReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      checkNo: llinsFamilyReport.checkNo,
      maleIndividuals: llinsFamilyReport.maleIndividuals,
      femaleIndividuals: llinsFamilyReport.femaleIndividuals,
      lessThan5Males: llinsFamilyReport.lessThan5Males,
      lessThan5Females: llinsFamilyReport.lessThan5Females,
      pregnantWomen: llinsFamilyReport.pregnantWomen,
      quantityReceived: llinsFamilyReport.quantityReceived,
      familyType: llinsFamilyReport.familyType,
      comment: llinsFamilyReport.comment,
      createdBy: llinsFamilyReport.createdBy,
      lastUpdatedBy: llinsFamilyReport.lastUpdatedBy,
      dayReached: llinsFamilyReport.dayReached,
      targetDetails: llinsFamilyReport.targetDetails,
      executingTeam: llinsFamilyReport.executingTeam,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      llinsFamilyReport.createdBy,
      llinsFamilyReport.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      llinsFamilyReport.dayReached
    );
    this.llinsFamilyTargetsSharedCollection = this.llinsFamilyTargetService.addLlinsFamilyTargetToCollectionIfMissing(
      this.llinsFamilyTargetsSharedCollection,
      llinsFamilyReport.targetDetails
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, llinsFamilyReport.executingTeam);
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
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayReached')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.llinsFamilyTargetService
      .query()
      .pipe(map((res: HttpResponse<ILlinsFamilyTarget[]>) => res.body ?? []))
      .pipe(
        map((llinsFamilyTargets: ILlinsFamilyTarget[]) =>
          this.llinsFamilyTargetService.addLlinsFamilyTargetToCollectionIfMissing(
            llinsFamilyTargets,
            this.editForm.get('targetDetails')!.value
          )
        )
      )
      .subscribe((llinsFamilyTargets: ILlinsFamilyTarget[]) => (this.llinsFamilyTargetsSharedCollection = llinsFamilyTargets));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('executingTeam')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILlinsFamilyReport {
    return {
      ...new LlinsFamilyReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      checkNo: this.editForm.get(['checkNo'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      familyType: this.editForm.get(['familyType'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      targetDetails: this.editForm.get(['targetDetails'])!.value,
      executingTeam: this.editForm.get(['executingTeam'])!.value,
    };
  }
}
