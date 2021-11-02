import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelativePeriods } from '../relative-periods.model';
import { RelativePeriodsService } from '../service/relative-periods.service';
import { RelativePeriodsDeleteDialogComponent } from '../delete/relative-periods-delete-dialog.component';

@Component({
  selector: 'app-relative-periods',
  templateUrl: './relative-periods.component.html',
})
export class RelativePeriodsComponent implements OnInit {
  relativePeriods?: IRelativePeriods[];
  isLoading = false;

  constructor(protected relativePeriodsService: RelativePeriodsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.relativePeriodsService.query().subscribe(
      (res: HttpResponse<IRelativePeriods[]>) => {
        this.isLoading = false;
        this.relativePeriods = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRelativePeriods): number {
    return item.id!;
  }

  delete(relativePeriods: IRelativePeriods): void {
    const modalRef = this.modalService.open(RelativePeriodsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.relativePeriods = relativePeriods;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
