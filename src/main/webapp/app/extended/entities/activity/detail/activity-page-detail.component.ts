import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivity } from "../../../../entities/activity/activity.model";

@Component({
  selector: 'app-activity-page-detail',
  templateUrl: './activity-page-detail.component.html',
})
export class ActivityPageDetailComponent implements OnInit {
  activity: IActivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.activity = activity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
