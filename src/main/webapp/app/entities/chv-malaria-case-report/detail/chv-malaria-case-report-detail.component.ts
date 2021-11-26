import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChvMalariaCaseReport } from '../chv-malaria-case-report.model';

@Component({
  selector: 'app-chv-malaria-case-report-detail',
  templateUrl: './chv-malaria-case-report-detail.component.html',
})
export class ChvMalariaCaseReportDetailComponent implements OnInit {
  chvMalariaCaseReport: IChvMalariaCaseReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvMalariaCaseReport }) => {
      this.chvMalariaCaseReport = chvMalariaCaseReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
