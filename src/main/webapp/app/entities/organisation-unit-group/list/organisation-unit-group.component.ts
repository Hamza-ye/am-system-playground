import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnitGroup } from '../organisation-unit-group.model';
import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';
import { OrganisationUnitGroupDeleteDialogComponent } from '../delete/organisation-unit-group-delete-dialog.component';

@Component({
  selector: 'app-organisation-unit-group',
  templateUrl: './organisation-unit-group.component.html',
})
export class OrganisationUnitGroupComponent implements OnInit {
  organisationUnitGroups?: IOrganisationUnitGroup[];
  isLoading = false;

  constructor(protected organisationUnitGroupService: OrganisationUnitGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.organisationUnitGroupService.query().subscribe(
      (res: HttpResponse<IOrganisationUnitGroup[]>) => {
        this.isLoading = false;
        this.organisationUnitGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrganisationUnitGroup): number {
    return item.id!;
  }

  delete(organisationUnitGroup: IOrganisationUnitGroup): void {
    const modalRef = this.modalService.open(OrganisationUnitGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.organisationUnitGroup = organisationUnitGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
