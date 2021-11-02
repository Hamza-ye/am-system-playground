import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMalariaCasesReport, MalariaCasesReport } from '../malaria-cases-report.model';
import { MalariaCasesReportService } from '../service/malaria-cases-report.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IDataSet } from 'app/entities/data-set/data-set.model';
import { DataSetService } from 'app/entities/data-set/service/data-set.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

@Component({
  selector: 'app-malaria-cases-report-update',
  templateUrl: './malaria-cases-report-update.component.html',
})
export class MalariaCasesReportUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  casesReportClassesSharedCollection: ICasesReportClass[] = [];
  periodsSharedCollection: IPeriod[] = [];
  dataSetsSharedCollection: IDataSet[] = [];
  organisationUnitsSharedCollection: IOrganisationUnit[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    rdtTested: [],
    rdtPositive: [],
    rdtPf: [],
    rdtPv: [],
    rdtPother: [],
    microTested: [],
    microPositive: [],
    microPf: [],
    microPv: [],
    microMix: [],
    microPother: [],
    probableCases: [],
    inpatientCases: [],
    deathCases: [],
    treated: [],
    suspectedCases: [],
    totalFrequents: [],
    comment: [],
    user: [],
    lastUpdatedBy: [],
    reportClass: [null, Validators.required],
    period: [null, Validators.required],
    dataSet: [],
    organisationUnit: [],
  });

  constructor(
    protected malariaCasesReportService: MalariaCasesReportService,
    protected userService: UserService,
    protected casesReportClassService: CasesReportClassService,
    protected periodService: PeriodService,
    protected dataSetService: DataSetService,
    protected organisationUnitService: OrganisationUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaCasesReport }) => {
      if (malariaCasesReport.id === undefined) {
        const today = dayjs().startOf('day');
        malariaCasesReport.created = today;
        malariaCasesReport.lastUpdated = today;
      }

      this.updateForm(malariaCasesReport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const malariaCasesReport = this.createFromForm();
    if (malariaCasesReport.id !== undefined) {
      this.subscribeToSaveResponse(this.malariaCasesReportService.update(malariaCasesReport));
    } else {
      this.subscribeToSaveResponse(this.malariaCasesReportService.create(malariaCasesReport));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCasesReportClassById(index: number, item: ICasesReportClass): number {
    return item.id!;
  }

  trackPeriodById(index: number, item: IPeriod): number {
    return item.id!;
  }

  trackDataSetById(index: number, item: IDataSet): number {
    return item.id!;
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMalariaCasesReport>>): void {
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

  protected updateForm(malariaCasesReport: IMalariaCasesReport): void {
    this.editForm.patchValue({
      id: malariaCasesReport.id,
      uid: malariaCasesReport.uid,
      created: malariaCasesReport.created ? malariaCasesReport.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: malariaCasesReport.lastUpdated ? malariaCasesReport.lastUpdated.format(DATE_TIME_FORMAT) : null,
      rdtTested: malariaCasesReport.rdtTested,
      rdtPositive: malariaCasesReport.rdtPositive,
      rdtPf: malariaCasesReport.rdtPf,
      rdtPv: malariaCasesReport.rdtPv,
      rdtPother: malariaCasesReport.rdtPother,
      microTested: malariaCasesReport.microTested,
      microPositive: malariaCasesReport.microPositive,
      microPf: malariaCasesReport.microPf,
      microPv: malariaCasesReport.microPv,
      microMix: malariaCasesReport.microMix,
      microPother: malariaCasesReport.microPother,
      probableCases: malariaCasesReport.probableCases,
      inpatientCases: malariaCasesReport.inpatientCases,
      deathCases: malariaCasesReport.deathCases,
      treated: malariaCasesReport.treated,
      suspectedCases: malariaCasesReport.suspectedCases,
      totalFrequents: malariaCasesReport.totalFrequents,
      comment: malariaCasesReport.comment,
      user: malariaCasesReport.user,
      lastUpdatedBy: malariaCasesReport.lastUpdatedBy,
      reportClass: malariaCasesReport.reportClass,
      period: malariaCasesReport.period,
      dataSet: malariaCasesReport.dataSet,
      organisationUnit: malariaCasesReport.organisationUnit,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      malariaCasesReport.user,
      malariaCasesReport.lastUpdatedBy
    );
    this.casesReportClassesSharedCollection = this.casesReportClassService.addCasesReportClassToCollectionIfMissing(
      this.casesReportClassesSharedCollection,
      malariaCasesReport.reportClass
    );
    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing(
      this.periodsSharedCollection,
      malariaCasesReport.period
    );
    this.dataSetsSharedCollection = this.dataSetService.addDataSetToCollectionIfMissing(
      this.dataSetsSharedCollection,
      malariaCasesReport.dataSet
    );
    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      malariaCasesReport.organisationUnit
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

    this.casesReportClassService
      .query()
      .pipe(map((res: HttpResponse<ICasesReportClass[]>) => res.body ?? []))
      .pipe(
        map((casesReportClasses: ICasesReportClass[]) =>
          this.casesReportClassService.addCasesReportClassToCollectionIfMissing(casesReportClasses, this.editForm.get('reportClass')!.value)
        )
      )
      .subscribe((casesReportClasses: ICasesReportClass[]) => (this.casesReportClassesSharedCollection = casesReportClasses));

    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing(periods, this.editForm.get('period')!.value)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));

    this.dataSetService
      .query()
      .pipe(map((res: HttpResponse<IDataSet[]>) => res.body ?? []))
      .pipe(
        map((dataSets: IDataSet[]) => this.dataSetService.addDataSetToCollectionIfMissing(dataSets, this.editForm.get('dataSet')!.value))
      )
      .subscribe((dataSets: IDataSet[]) => (this.dataSetsSharedCollection = dataSets));

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
  }

  protected createFromForm(): IMalariaCasesReport {
    return {
      ...new MalariaCasesReport(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      rdtTested: this.editForm.get(['rdtTested'])!.value,
      rdtPositive: this.editForm.get(['rdtPositive'])!.value,
      rdtPf: this.editForm.get(['rdtPf'])!.value,
      rdtPv: this.editForm.get(['rdtPv'])!.value,
      rdtPother: this.editForm.get(['rdtPother'])!.value,
      microTested: this.editForm.get(['microTested'])!.value,
      microPositive: this.editForm.get(['microPositive'])!.value,
      microPf: this.editForm.get(['microPf'])!.value,
      microPv: this.editForm.get(['microPv'])!.value,
      microMix: this.editForm.get(['microMix'])!.value,
      microPother: this.editForm.get(['microPother'])!.value,
      probableCases: this.editForm.get(['probableCases'])!.value,
      inpatientCases: this.editForm.get(['inpatientCases'])!.value,
      deathCases: this.editForm.get(['deathCases'])!.value,
      treated: this.editForm.get(['treated'])!.value,
      suspectedCases: this.editForm.get(['suspectedCases'])!.value,
      totalFrequents: this.editForm.get(['totalFrequents'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      reportClass: this.editForm.get(['reportClass'])!.value,
      period: this.editForm.get(['period'])!.value,
      dataSet: this.editForm.get(['dataSet'])!.value,
      organisationUnit: this.editForm.get(['organisationUnit'])!.value,
    };
  }
}
