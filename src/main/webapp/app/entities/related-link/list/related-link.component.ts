import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatedLink } from '../related-link.model';
import { RelatedLinkService } from '../service/related-link.service';
import { RelatedLinkDeleteDialogComponent } from '../delete/related-link-delete-dialog.component';

@Component({
  selector: 'app-related-link',
  templateUrl: './related-link.component.html',
})
export class RelatedLinkComponent implements OnInit {
  relatedLinks?: IRelatedLink[];
  isLoading = false;

  constructor(protected relatedLinkService: RelatedLinkService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.relatedLinkService.query().subscribe(
      (res: HttpResponse<IRelatedLink[]>) => {
        this.isLoading = false;
        this.relatedLinks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRelatedLink): number {
    return item.id!;
  }

  delete(relatedLink: IRelatedLink): void {
    const modalRef = this.modalService.open(RelatedLinkDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.relatedLink = relatedLink;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
