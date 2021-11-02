import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnitGroupSet } from '../organisation-unit-group-set.model';
import { OrganisationUnitGroupSetService } from '../service/organisation-unit-group-set.service';
import { OrganisationUnitGroupSetDeleteDialogComponent } from '../delete/organisation-unit-group-set-delete-dialog.component';

@Component({
  selector: 'app-organisation-unit-group-set',
  templateUrl: './organisation-unit-group-set.component.html',
})
export class OrganisationUnitGroupSetComponent implements OnInit {
  organisationUnitGroupSets?: IOrganisationUnitGroupSet[];
  isLoading = false;

  constructor(protected organisationUnitGroupSetService: OrganisationUnitGroupSetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.organisationUnitGroupSetService.query().subscribe(
      (res: HttpResponse<IOrganisationUnitGroupSet[]>) => {
        this.isLoading = false;
        this.organisationUnitGroupSets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrganisationUnitGroupSet): number {
    return item.id!;
  }

  delete(organisationUnitGroupSet: IOrganisationUnitGroupSet): void {
    const modalRef = this.modalService.open(OrganisationUnitGroupSetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.organisationUnitGroupSet = organisationUnitGroupSet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
