import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILLINSVillageReport } from '../llins-village-report.model';

@Component({
  selector: 'app-llins-village-report-detail',
  templateUrl: './llins-village-report-detail.component.html',
})
export class LLINSVillageReportDetailComponent implements OnInit {
  lLINSVillageReport: ILLINSVillageReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageReport }) => {
      this.lLINSVillageReport = lLINSVillageReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
