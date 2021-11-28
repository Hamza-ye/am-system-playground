import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatedLink } from '../related-link.model';
import { RelatedLinkService } from '../service/related-link.service';

@Component({
  templateUrl: './related-link-delete-dialog.component.html',
})
export class RelatedLinkDeleteDialogComponent {
  relatedLink?: IRelatedLink;

  constructor(protected relatedLinkService: RelatedLinkService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.relatedLinkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
