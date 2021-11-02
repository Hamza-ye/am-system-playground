import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStatusOfCoverage } from '../status-of-coverage.model';
import { StatusOfCoverageService } from '../service/status-of-coverage.service';
import { StatusOfCoverageDeleteDialogComponent } from '../delete/status-of-coverage-delete-dialog.component';

@Component({
  selector: 'app-status-of-coverage',
  templateUrl: './status-of-coverage.component.html',
})
export class StatusOfCoverageComponent implements OnInit {
  statusOfCoverages?: IStatusOfCoverage[];
  isLoading = false;

  constructor(protected statusOfCoverageService: StatusOfCoverageService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.statusOfCoverageService.query().subscribe(
      (res: HttpResponse<IStatusOfCoverage[]>) => {
        this.isLoading = false;
        this.statusOfCoverages = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStatusOfCoverage): number {
    return item.id!;
  }

  delete(statusOfCoverage: IStatusOfCoverage): void {
    const modalRef = this.modalService.open(StatusOfCoverageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.statusOfCoverage = statusOfCoverage;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
