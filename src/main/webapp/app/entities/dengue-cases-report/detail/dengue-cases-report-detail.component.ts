import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDengueCasesReport } from '../dengue-cases-report.model';

@Component({
  selector: 'app-dengue-cases-report-detail',
  templateUrl: './dengue-cases-report-detail.component.html',
})
export class DengueCasesReportDetailComponent implements OnInit {
  dengueCasesReport: IDengueCasesReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dengueCasesReport }) => {
      this.dengueCasesReport = dengueCasesReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
