import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILlinsVillageReport } from '../llins-village-report.model';

@Component({
  selector: 'app-llins-village-report-detail',
  templateUrl: './llins-village-report-detail.component.html',
})
export class LlinsVillageReportDetailComponent implements OnInit {
  llinsVillageReport: ILlinsVillageReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsVillageReport }) => {
      this.llinsVillageReport = llinsVillageReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
