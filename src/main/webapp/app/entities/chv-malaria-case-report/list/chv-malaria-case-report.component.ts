import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvMalariaCaseReport } from '../chv-malaria-case-report.model';
import { ChvMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { ChvMalariaCaseReportDeleteDialogComponent } from '../delete/chv-malaria-case-report-delete-dialog.component';

@Component({
  selector: 'app-chv-malaria-case-report',
  templateUrl: './chv-malaria-case-report.component.html',
})
export class ChvMalariaCaseReportComponent implements OnInit {
  chvMalariaCaseReports?: IChvMalariaCaseReport[];
  isLoading = false;

  constructor(protected chvMalariaCaseReportService: ChvMalariaCaseReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.chvMalariaCaseReportService.query().subscribe(
      (res: HttpResponse<IChvMalariaCaseReport[]>) => {
        this.isLoading = false;
        this.chvMalariaCaseReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IChvMalariaCaseReport): number {
    return item.id!;
  }

  delete(chvMalariaCaseReport: IChvMalariaCaseReport): void {
    const modalRef = this.modalService.open(ChvMalariaCaseReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chvMalariaCaseReport = chvMalariaCaseReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
