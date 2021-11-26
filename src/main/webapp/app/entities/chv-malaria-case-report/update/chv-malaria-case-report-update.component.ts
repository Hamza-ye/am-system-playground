import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IChvMalariaCaseReport, ChvMalariaCaseReport } from '../chv-malaria-case-report.model';
import { ChvMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';

@Component({
  selector: 'app-chv-malaria-case-report-update',
  templateUrl: './chv-malaria-case-report-update.component.html',
})
export class ChvMalariaCaseReportUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];
  chvsSharedCollection: IChv[] = [];
  casesReportClassesSharedCollection: ICasesReportClass[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    date: [],
    individualName: [],
    gender: [],
    isPregnant: [],
    malariaTestResult: [],
    drugsGiven: [],
    suppsGiven: [],
    referral: [],
    barImageUrl: [],
    comment: [],
    subVillage: [],
    createdBy: [],
    lastUpdatedBy: [],
    chv: [null, Validators.required],
    reportClass: [null, Validators.required],
  });

  constructor(
    protected chvMalariaCaseReportService: ChvMalariaCaseReportService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected chvService: ChvService,
    protected casesReportClassService: CasesReportClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvMalariaCaseReport }) => {
      if (chvMalariaCaseReport.id === undefined) {
        const today = dayjs().startOf('day');
        chvMalariaCaseReport.created = today;
        chvMalariaCaseReport.lastUpdated = today;
      }

      this.updateForm(chvMalariaCaseReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chvMalariaCaseReport = this.createFromForm();
    if (chvMalariaCaseReport.id !== undefined) {
      this.subscribeToSaveResponse(this.chvMalariaCaseReportService.update(chvMalariaCaseReport));
    } else {
      this.subscribeToSaveResponse(this.chvMalariaCaseReportService.create(chvMalariaCaseReport));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackChvById(index: number, item: IChv): number {
    return item.id!;
  }

  trackCasesReportClassById(index: number, item: ICasesReportClass): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChvMalariaCaseReport>>): void {
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

  protected updateForm(chvMalariaCaseReport: IChvMalariaCaseReport): void {
    this.editForm.patchValue({
      id: chvMalariaCaseReport.id,
      uid: chvMalariaCaseReport.uid,
      created: chvMalariaCaseReport.created ? chvMalariaCaseReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: chvMalariaCaseReport.lastUpdated ? chvMalariaCaseReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      date: chvMalariaCaseReport.date,
      individualName: chvMalariaCaseReport.individualName,
      gender: chvMalariaCaseReport.gender,
      isPregnant: chvMalariaCaseReport.isPregnant,
      malariaTestResult: chvMalariaCaseReport.malariaTestResult,
      drugsGiven: chvMalariaCaseReport.drugsGiven,
      suppsGiven: chvMalariaCaseReport.suppsGiven,
      referral: chvMalariaCaseReport.referral,
      barImageUrl: chvMalariaCaseReport.barImageUrl,
      comment: chvMalariaCaseReport.comment,
      subVillage: chvMalariaCaseReport.subVillage,
      createdBy: chvMalariaCaseReport.createdBy,
      lastUpdatedBy: chvMalariaCaseReport.lastUpdatedBy,
      chv: chvMalariaCaseReport.chv,
      reportClass: chvMalariaCaseReport.reportClass,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      chvMalariaCaseReport.subVillage
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      chvMalariaCaseReport.createdBy,
      chvMalariaCaseReport.lastUpdatedBy
    );
    this.chvsSharedCollection = this.chvService.addChvToCollectionIfMissing(this.chvsSharedCollection, chvMalariaCaseReport.chv);
    this.casesReportClassesSharedCollection = this.casesReportClassService.addCasesReportClassToCollectionIfMissing(
      this.casesReportClassesSharedCollection,
      chvMalariaCaseReport.reportClass
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organisationUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrganisationUnit[]>) => res.body ?? []))
      .pipe(
        map((organisationUnits: IOrganisationUnit[]) =>
          this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(organisationUnits, this.editForm.get('subVillage')!.value)
        )
      )
      .subscribe((organisationUnits: IOrganisationUnit[]) => (this.organisationUnitsSharedCollection = organisationUnits));

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

    this.chvService
      .query()
      .pipe(map((res: HttpResponse<IChv[]>) => res.body ?? []))
      .pipe(map((chvs: IChv[]) => this.chvService.addChvToCollectionIfMissing(chvs, this.editForm.get('chv')!.value)))
      .subscribe((chvs: IChv[]) => (this.chvsSharedCollection = chvs));

    this.casesReportClassService
      .query()
      .pipe(map((res: HttpResponse<ICasesReportClass[]>) => res.body ?? []))
      .pipe(
        map((casesReportClasses: ICasesReportClass[]) =>
          this.casesReportClassService.addCasesReportClassToCollectionIfMissing(casesReportClasses, this.editForm.get('reportClass')!.value)
        )
      )
      .subscribe((casesReportClasses: ICasesReportClass[]) => (this.casesReportClassesSharedCollection = casesReportClasses));
  }

  protected createFromForm(): IChvMalariaCaseReport {
    return {
      ...new ChvMalariaCaseReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      date: this.editForm.get(['date'])!.value,
      individualName: this.editForm.get(['individualName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      isPregnant: this.editForm.get(['isPregnant'])!.value,
      malariaTestResult: this.editForm.get(['malariaTestResult'])!.value,
      drugsGiven: this.editForm.get(['drugsGiven'])!.value,
      suppsGiven: this.editForm.get(['suppsGiven'])!.value,
      referral: this.editForm.get(['referral'])!.value,
      barImageUrl: this.editForm.get(['barImageUrl'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      subVillage: this.editForm.get(['subVillage'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      chv: this.editForm.get(['chv'])!.value,
      reportClass: this.editForm.get(['reportClass'])!.value,
    };
  }
}
