import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkingDay } from '../working-day.model';

@Component({
  selector: 'app-working-day-detail',
  templateUrl: './working-day-detail.component.html',
})
export class WorkingDayDetailComponent implements OnInit {
  workingDay: IWorkingDay | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingDay }) => {
      this.workingDay = workingDay;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
