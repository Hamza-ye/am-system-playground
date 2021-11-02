import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemographicData } from '../demographic-data.model';
import { DemographicDataService } from '../service/demographic-data.service';
import { DemographicDataDeleteDialogComponent } from '../delete/demographic-data-delete-dialog.component';

@Component({
  selector: 'app-demographic-data',
  templateUrl: './demographic-data.component.html',
})
export class DemographicDataComponent implements OnInit {
  demographicData?: IDemographicData[];
  isLoading = false;

  constructor(protected demographicDataService: DemographicDataService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.demographicDataService.query().subscribe(
      (res: HttpResponse<IDemographicData[]>) => {
        this.isLoading = false;
        this.demographicData = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDemographicData): number {
    return item.id!;
  }

  delete(demographicData: IDemographicData): void {
    const modalRef = this.modalService.open(DemographicDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demographicData = demographicData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
