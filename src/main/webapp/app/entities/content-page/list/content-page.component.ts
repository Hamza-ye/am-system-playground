import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContentPage } from '../content-page.model';
import { ContentPageService } from '../service/content-page.service';
import { ContentPageDeleteDialogComponent } from '../delete/content-page-delete-dialog.component';

@Component({
  selector: 'app-content-page',
  templateUrl: './content-page.component.html',
})
export class ContentPageComponent implements OnInit {
  contentPages?: IContentPage[];
  isLoading = false;

  constructor(protected contentPageService: ContentPageService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contentPageService.query().subscribe(
      (res: HttpResponse<IContentPage[]>) => {
        this.isLoading = false;
        this.contentPages = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContentPage): number {
    return item.id!;
  }

  delete(contentPage: IContentPage): void {
    const modalRef = this.modalService.open(ContentPageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contentPage = contentPage;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
