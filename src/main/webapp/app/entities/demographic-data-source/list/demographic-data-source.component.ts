import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemographicDataSource } from '../demographic-data-source.model';
import { DemographicDataSourceService } from '../service/demographic-data-source.service';
import { DemographicDataSourceDeleteDialogComponent } from '../delete/demographic-data-source-delete-dialog.component';

@Component({
  selector: 'app-demographic-data-source',
  templateUrl: './demographic-data-source.component.html',
})
export class DemographicDataSourceComponent implements OnInit {
  demographicDataSources?: IDemographicDataSource[];
  isLoading = false;

  constructor(protected demographicDataSourceService: DemographicDataSourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.demographicDataSourceService.query().subscribe(
      (res: HttpResponse<IDemographicDataSource[]>) => {
        this.isLoading = false;
        this.demographicDataSources = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDemographicDataSource): number {
    return item.id!;
  }

  delete(demographicDataSource: IDemographicDataSource): void {
    const modalRef = this.modalService.open(DemographicDataSourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demographicDataSource = demographicDataSource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
