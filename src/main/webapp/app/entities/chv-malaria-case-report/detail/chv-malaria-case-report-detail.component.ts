import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICHVMalariaCaseReport } from '../chv-malaria-case-report.model';

@Component({
  selector: 'app-chv-malaria-case-report-detail',
  templateUrl: './chv-malaria-case-report-detail.component.html',
})
export class CHVMalariaCaseReportDetailComponent implements OnInit {
  cHVMalariaCaseReport: ICHVMalariaCaseReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVMalariaCaseReport }) => {
      this.cHVMalariaCaseReport = cHVMalariaCaseReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
