import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFingerprint, Fingerprint } from '../fingerprint.model';
import { FingerprintService } from '../service/fingerprint.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

@Component({
  selector: 'app-fingerprint-update',
  templateUrl: './fingerprint-update.component.html',
})
export class FingerprintUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  familiesSharedCollection: IFamily[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    description: [],
    created: [],
    lastUpdated: [],
    fingerprintUrl: [],
    fingerprintOwner: [],
    createdBy: [],
    lastUpdatedBy: [],
    family: [],
  });

  constructor(
    protected fingerprintService: FingerprintService,
    protected userService: UserService,
    protected familyService: FamilyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fingerprint }) => {
      if (fingerprint.id === undefined) {
        const today = dayjs().startOf('day');
        fingerprint.created = today;
        fingerprint.lastUpdated = today;
      }

      this.updateForm(fingerprint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fingerprint = this.createFromForm();
    if (fingerprint.id !== undefined) {
      this.subscribeToSaveResponse(this.fingerprintService.update(fingerprint));
    } else {
      this.subscribeToSaveResponse(this.fingerprintService.create(fingerprint));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFamilyById(index: number, item: IFamily): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFingerprint>>): void {
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

  protected updateForm(fingerprint: IFingerprint): void {
    this.editForm.patchValue({
      id: fingerprint.id,
      uid: fingerprint.uid,
      description: fingerprint.description,
      created: fingerprint.created ? fingerprint.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: fingerprint.lastUpdated ? fingerprint.lastUpdated.format(DATE_TIME_FORMAT) : null,
      fingerprintUrl: fingerprint.fingerprintUrl,
      fingerprintOwner: fingerprint.fingerprintOwner,
      createdBy: fingerprint.createdBy,
      lastUpdatedBy: fingerprint.lastUpdatedBy,
      family: fingerprint.family,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      fingerprint.createdBy,
      fingerprint.lastUpdatedBy
    );
    this.familiesSharedCollection = this.familyService.addFamilyToCollectionIfMissing(this.familiesSharedCollection, fingerprint.family);
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

    this.familyService
      .query()
      .pipe(map((res: HttpResponse<IFamily[]>) => res.body ?? []))
      .pipe(map((families: IFamily[]) => this.familyService.addFamilyToCollectionIfMissing(families, this.editForm.get('family')!.value)))
      .subscribe((families: IFamily[]) => (this.familiesSharedCollection = families));
  }

  protected createFromForm(): IFingerprint {
    return {
      ...new Fingerprint(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fingerprintUrl: this.editForm.get(['fingerprintUrl'])!.value,
      fingerprintOwner: this.editForm.get(['fingerprintOwner'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      family: this.editForm.get(['family'])!.value,
    };
  }
}
