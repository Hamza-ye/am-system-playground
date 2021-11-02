import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataProvider } from '../data-provider.model';
import { DataProviderService } from '../service/data-provider.service';
import { DataProviderDeleteDialogComponent } from '../delete/data-provider-delete-dialog.component';

@Component({
  selector: 'app-data-provider',
  templateUrl: './data-provider.component.html',
})
export class DataProviderComponent implements OnInit {
  dataProviders?: IDataProvider[];
  isLoading = false;

  constructor(protected dataProviderService: DataProviderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dataProviderService.query().subscribe(
      (res: HttpResponse<IDataProvider[]>) => {
        this.isLoading = false;
        this.dataProviders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDataProvider): number {
    return item.id!;
  }

  delete(dataProvider: IDataProvider): void {
    const modalRef = this.modalService.open(DataProviderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dataProvider = dataProvider;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
