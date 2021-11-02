import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingDay } from '../working-day.model';
import { WorkingDayService } from '../service/working-day.service';
import { WorkingDayDeleteDialogComponent } from '../delete/working-day-delete-dialog.component';

@Component({
  selector: 'app-working-day',
  templateUrl: './working-day.component.html',
})
export class WorkingDayComponent implements OnInit {
  workingDays?: IWorkingDay[];
  isLoading = false;

  constructor(protected workingDayService: WorkingDayService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.workingDayService.query().subscribe(
      (res: HttpResponse<IWorkingDay[]>) => {
        this.isLoading = false;
        this.workingDays = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  delete(workingDay: IWorkingDay): void {
    const modalRef = this.modalService.open(WorkingDayDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.workingDay = workingDay;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
