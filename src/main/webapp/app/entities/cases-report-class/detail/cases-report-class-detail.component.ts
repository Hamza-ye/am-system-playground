import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICasesReportClass } from '../cases-report-class.model';

@Component({
  selector: 'app-cases-report-class-detail',
  templateUrl: './cases-report-class-detail.component.html',
})
export class CasesReportClassDetailComponent implements OnInit {
  casesReportClass: ICasesReportClass | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ casesReportClass }) => {
      this.casesReportClass = casesReportClass;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
