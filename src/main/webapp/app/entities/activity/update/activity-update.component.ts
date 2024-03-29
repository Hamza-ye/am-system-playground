import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IActivity, Activity } from '../activity.model';
import { ActivityService } from '../service/activity.service';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { ContentPageService } from 'app/entities/content-page/service/content-page.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

@Component({
  selector: 'app-activity-update',
  templateUrl: './activity-update.component.html',
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;

  contentPagesCollection: IContentPage[] = [];
  usersSharedCollection: IUser[] = [];
  projectsSharedCollection: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    active: [],
    displayed: [],
    contentPage: [],
    createdBy: [],
    lastUpdatedBy: [],
    project: [null, Validators.required],
  });

  constructor(
    protected activityService: ActivityService,
    protected contentPageService: ContentPageService,
    protected userService: UserService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      if (activity.id === undefined) {
        const today = dayjs().startOf('day');
        activity.created = today;
        activity.lastUpdated = today;
      }

      this.updateForm(activity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.createFromForm();
    if (activity.id !== undefined) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
    }
  }

  trackContentPageById(index: number, item: IContentPage): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProjectById(index: number, item: IProject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivity>>): void {
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

  protected updateForm(activity: IActivity): void {
    this.editForm.patchValue({
      id: activity.id,
      uid: activity.uid,
      code: activity.code,
      name: activity.name,
      created: activity.created ? activity.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: activity.lastUpdated ? activity.lastUpdated.format(DATE_TIME_FORMAT) : null,
      startDate: activity.startDate,
      endDate: activity.endDate,
      active: activity.active,
      displayed: activity.displayed,
      contentPage: activity.contentPage,
      createdBy: activity.createdBy,
      lastUpdatedBy: activity.lastUpdatedBy,
      project: activity.project,
    });

    this.contentPagesCollection = this.contentPageService.addContentPageToCollectionIfMissing(
      this.contentPagesCollection,
      activity.contentPage
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      activity.createdBy,
      activity.lastUpdatedBy
    );
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing(this.projectsSharedCollection, activity.project);
  }

  protected loadRelationshipsOptions(): void {
    this.contentPageService
      .query({ filter: 'activity-is-null' })
      .pipe(map((res: HttpResponse<IContentPage[]>) => res.body ?? []))
      .pipe(
        map((contentPages: IContentPage[]) =>
          this.contentPageService.addContentPageToCollectionIfMissing(contentPages, this.editForm.get('contentPage')!.value)
        )
      )
      .subscribe((contentPages: IContentPage[]) => (this.contentPagesCollection = contentPages));

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

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing(projects, this.editForm.get('project')!.value))
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }

  protected createFromForm(): IActivity {
    return {
      ...new Activity(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      active: this.editForm.get(['active'])!.value,
      displayed: this.editForm.get(['displayed'])!.value,
      contentPage: this.editForm.get(['contentPage'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      project: this.editForm.get(['project'])!.value,
    };
  }
}
