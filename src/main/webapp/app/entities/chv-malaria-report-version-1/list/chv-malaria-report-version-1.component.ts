import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { ChvMalariaReportVersion1DeleteDialogComponent } from '../delete/chv-malaria-report-version-1-delete-dialog.component';

@Component({
  selector: 'app-chv-malaria-report-version-1',
  templateUrl: './chv-malaria-report-version-1.component.html',
})
export class ChvMalariaReportVersion1Component implements OnInit {
  chvMalariaReportVersion1s?: IChvMalariaReportVersion1[];
  isLoading = false;

  constructor(protected chvMalariaReportVersion1Service: ChvMalariaReportVersion1Service, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.chvMalariaReportVersion1Service.query().subscribe(
      (res: HttpResponse<IChvMalariaReportVersion1[]>) => {
        this.isLoading = false;
        this.chvMalariaReportVersion1s = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IChvMalariaReportVersion1): number {
    return item.id!;
  }

  delete(chvMalariaReportVersion1: IChvMalariaReportVersion1): void {
    const modalRef = this.modalService.open(ChvMalariaReportVersion1DeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chvMalariaReportVersion1 = chvMalariaReportVersion1;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
