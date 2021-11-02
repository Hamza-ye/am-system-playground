import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDemographicData, DemographicData } from '../demographic-data.model';
import { DemographicDataService } from '../service/demographic-data.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDemographicDataSource } from 'app/entities/demographic-data-source/demographic-data-source.model';
import { DemographicDataSourceService } from 'app/entities/demographic-data-source/service/demographic-data-source.service';

@Component({
  selector: 'app-demographic-data-update',
  templateUrl: './demographic-data-update.component.html',
})
export class DemographicDataUpdateComponent implements OnInit {
  isSaving = false;

  organisationUnitsSharedCollection: IOrganisationUnit[] = [];
  usersSharedCollection: IUser[] = [];
  demographicDataSourcesSharedCollection: IDemographicDataSource[] = [];

  editForm = this.fb.group({
    id: [],
    created: [],
    lastUpdated: [],
    date: [null, [Validators.required]],
    level: [],
    totalPopulation: [],
    malePopulation: [],
    femalePopulation: [],
    lessThan5Population: [],
    greaterThan5Population: [],
    bw5And15Population: [],
    greaterThan15Population: [],
    household: [],
    houses: [],
    healthFacilities: [],
    avgNoOfRooms: [],
    avgRoomArea: [],
    avgHouseArea: [],
    individualsPerHousehold: [],
    populationGrowthRate: [],
    comment: [],
    organisationUnit: [],
    user: [],
    lastUpdatedBy: [],
    source: [null, Validators.required],
  });

  constructor(
    protected demographicDataService: DemographicDataService,
    protected organisationUnitService: OrganisationUnitService,
    protected userService: UserService,
    protected demographicDataSourceService: DemographicDataSourceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demographicData }) => {
      if (demographicData.id === undefined) {
        const today = dayjs().startOf('day');
        demographicData.created = today;
        demographicData.lastUpdated = today;
      }

      this.updateForm(demographicData);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demographicData = this.createFromForm();
    if (demographicData.id !== undefined) {
      this.subscribeToSaveResponse(this.demographicDataService.update(demographicData));
    } else {
      this.subscribeToSaveResponse(this.demographicDataService.create(demographicData));
    }
  }

  trackOrganisationUnitById(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDemographicDataSourceById(index: number, item: IDemographicDataSource): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemographicData>>): void {
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

  protected updateForm(demographicData: IDemographicData): void {
    this.editForm.patchValue({
      id: demographicData.id,
      created: demographicData.created ? demographicData.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: demographicData.lastUpdated ? demographicData.lastUpdated.format(DATE_TIME_FORMAT) : null,
      date: demographicData.date,
      level: demographicData.level,
      totalPopulation: demographicData.totalPopulation,
      malePopulation: demographicData.malePopulation,
      femalePopulation: demographicData.femalePopulation,
      lessThan5Population: demographicData.lessThan5Population,
      greaterThan5Population: demographicData.greaterThan5Population,
      bw5And15Population: demographicData.bw5And15Population,
      greaterThan15Population: demographicData.greaterThan15Population,
      household: demographicData.household,
      houses: demographicData.houses,
      healthFacilities: demographicData.healthFacilities,
      avgNoOfRooms: demographicData.avgNoOfRooms,
      avgRoomArea: demographicData.avgRoomArea,
      avgHouseArea: demographicData.avgHouseArea,
      individualsPerHousehold: demographicData.individualsPerHousehold,
      populationGrowthRate: demographicData.populationGrowthRate,
      comment: demographicData.comment,
      organisationUnit: demographicData.organisationUnit,
      user: demographicData.user,
      lastUpdatedBy: demographicData.lastUpdatedBy,
      source: demographicData.source,
    });

    this.organisationUnitsSharedCollection = this.organisationUnitService.addOrganisationUnitToCollectionIfMissing(
      this.organisationUnitsSharedCollection,
      demographicData.organisationUnit
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      demographicData.user,
      demographicData.lastUpdatedBy
    );
    this.demographicDataSourcesSharedCollection = this.demographicDataSourceService.addDemographicDataSourceToCollectionIfMissing(
      this.demographicDataSourcesSharedCollection,
      demographicData.source
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

    this.demographicDataSourceService
      .query()
      .pipe(map((res: HttpResponse<IDemographicDataSource[]>) => res.body ?? []))
      .pipe(
        map((demographicDataSources: IDemographicDataSource[]) =>
          this.demographicDataSourceService.addDemographicDataSourceToCollectionIfMissing(
            demographicDataSources,
            this.editForm.get('source')!.value
          )
        )
      )
      .subscribe(
        (demographicDataSources: IDemographicDataSource[]) => (this.demographicDataSourcesSharedCollection = demographicDataSources)
      );
  }

  protected createFromForm(): IDemographicData {
    return {
      ...new DemographicData(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      date: this.editForm.get(['date'])!.value,
      level: this.editForm.get(['level'])!.value,
      totalPopulation: this.editForm.get(['totalPopulation'])!.value,
      malePopulation: this.editForm.get(['malePopulation'])!.value,
      femalePopulation: this.editForm.get(['femalePopulation'])!.value,
      lessThan5Population: this.editForm.get(['lessThan5Population'])!.value,
      greaterThan5Population: this.editForm.get(['greaterThan5Population'])!.value,
      bw5And15Population: this.editForm.get(['bw5And15Population'])!.value,
      greaterThan15Population: this.editForm.get(['greaterThan15Population'])!.value,
      household: this.editForm.get(['household'])!.value,
      houses: this.editForm.get(['houses'])!.value,
      healthFacilities: this.editForm.get(['healthFacilities'])!.value,
      avgNoOfRooms: this.editForm.get(['avgNoOfRooms'])!.value,
      avgRoomArea: this.editForm.get(['avgRoomArea'])!.value,
      avgHouseArea: this.editForm.get(['avgHouseArea'])!.value,
      individualsPerHousehold: this.editForm.get(['individualsPerHousehold'])!.value,
      populationGrowthRate: this.editForm.get(['populationGrowthRate'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      organisationUnit: this.editForm.get(['organisationUnit'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      source: this.editForm.get(['source'])!.value,
    };
  }
}
