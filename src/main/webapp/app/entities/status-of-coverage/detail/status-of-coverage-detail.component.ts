import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStatusOfCoverage } from '../status-of-coverage.model';

@Component({
  selector: 'app-status-of-coverage-detail',
  templateUrl: './status-of-coverage-detail.component.html',
})
export class StatusOfCoverageDetailComponent implements OnInit {
  statusOfCoverage: IStatusOfCoverage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusOfCoverage }) => {
      this.statusOfCoverage = statusOfCoverage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
