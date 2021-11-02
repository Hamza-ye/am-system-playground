import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMalariaCasesReport } from '../malaria-cases-report.model';

@Component({
  selector: 'app-malaria-cases-report-detail',
  templateUrl: './malaria-cases-report-detail.component.html',
})
export class MalariaCasesReportDetailComponent implements OnInit {
  malariaCasesReport: IMalariaCasesReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ malariaCasesReport }) => {
      this.malariaCasesReport = malariaCasesReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
