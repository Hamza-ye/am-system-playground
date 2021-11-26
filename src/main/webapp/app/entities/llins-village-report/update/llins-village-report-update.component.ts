import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILlinsVillageReport, LlinsVillageReport } from '../llins-village-report.model';
import { LlinsVillageReportService } from '../service/llins-village-report.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { LlinsVillageTargetService } from 'app/entities/llins-village-target/service/llins-village-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'app-llins-village-report-update',
  templateUrl: './llins-village-report-update.component.html',
})
export class LlinsVillageReportUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  llinsVillageTargetsSharedCollection: ILlinsVillageTarget[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    houses: [null, [Validators.required, Validators.min(0)]],
    residentHousehold: [null, [Validators.required, Validators.min(0)]],
    idpsHousehold: [null, [Validators.required, Validators.min(0)]],
    maleIndividuals: [null, [Validators.min(0)]],
    femaleIndividuals: [null, [Validators.min(0)]],
    lessThan5Males: [null, [Validators.min(0)]],
    lessThan5Females: [null, [Validators.min(0)]],
    pregnantWomen: [null, [Validators.min(0)]],
    quantityReceived: [null, [Validators.required, Validators.min(0)]],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    targetDetails: [],
    executingTeam: [null, Validators.required],
  });

  constructor(
    protected llinsVillageReportService: LlinsVillageReportService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected llinsVillageTargetService: LlinsVillageTargetService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsVillageReport }) => {
      if (llinsVillageReport.id === undefined) {
        const today = dayjs().startOf('day');
        llinsVillageReport.created = today;
        llinsVillageReport.lastUpdated = today;
      }

      this.updateForm(llinsVillageReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const llinsVillageReport = this.createFromForm();
    if (llinsVillageReport.id !== undefined) {
      this.subscribeToSaveResponse(this.llinsVillageReportService.update(llinsVillageReport));
    } else {
      this.subscribeToSaveResponse(this.llinsVillageReportService.create(llinsVillageReport));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLlinsVillageTargetById(index: number, item: ILlinsVillageTarget): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILlinsVillageReport>>): void {
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

  protected updateForm(llinsVillageReport: ILlinsVillageReport): void {
    this.editForm.patchValue({
      id: llinsVillageReport.id,
      uid: llinsVillageReport.uid,
      created: llinsVillageReport.created ? llinsVillageReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: llinsVillageReport.lastUpdated ? llinsVillageReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      houses: llinsVillageReport.houses,
      residentHousehold: llinsVillageReport.residentHousehold,
      idpsHousehold: llinsVillageReport.idpsHousehold,
      maleIndividuals: llinsVillageReport.maleIndividuals,
      femaleIndividuals: llinsVillageReport.femaleIndividuals,
      lessThan5Males: llinsVillageReport.lessThan5Males,
      lessThan5Females: llinsVillageReport.lessThan5Females,
      pregnantWomen: llinsVillageReport.pregnantWomen,
      quantityReceived: llinsVillageReport.quantityReceived,
      comment: llinsVillageReport.comment,
      createdBy: llinsVillageReport.createdBy,
      lastUpdatedBy: llinsVillageReport.lastUpdatedBy,
      dayReached: llinsVillageReport.dayReached,
      targetDetails: llinsVillageReport.targetDetails,
      executingTeam: llinsVillageReport.executingTeam,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      llinsVillageReport.createdBy,
      llinsVillageReport.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      llinsVillageReport.dayReached
    );
    this.llinsVillageTargetsSharedCollection = this.llinsVillageTargetService.addLlinsVillageTargetToCollectionIfMissing(
      this.llinsVillageTargetsSharedCollection,
      llinsVillageReport.targetDetails
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(
      this.teamsSharedCollection,
      llinsVillageReport.executingTeam
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

    this.workingDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkingDay[]>) => res.body ?? []))
      .pipe(
        map((workingDays: IWorkingDay[]) =>
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayReached')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.llinsVillageTargetService
      .query()
      .pipe(map((res: HttpResponse<ILlinsVillageTarget[]>) => res.body ?? []))
      .pipe(
        map((llinsVillageTargets: ILlinsVillageTarget[]) =>
          this.llinsVillageTargetService.addLlinsVillageTargetToCollectionIfMissing(
            llinsVillageTargets,
            this.editForm.get('targetDetails')!.value
          )
        )
      )
      .subscribe((llinsVillageTargets: ILlinsVillageTarget[]) => (this.llinsVillageTargetsSharedCollection = llinsVillageTargets));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('executingTeam')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ILlinsVillageReport {
    return {
      ...new LlinsVillageReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      houses: this.editForm.get(['houses'])!.value,
      residentHousehold: this.editForm.get(['residentHousehold'])!.value,
      idpsHousehold: this.editForm.get(['idpsHousehold'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      targetDetails: this.editForm.get(['targetDetails'])!.value,
      executingTeam: this.editForm.get(['executingTeam'])!.value,
    };
  }
}
