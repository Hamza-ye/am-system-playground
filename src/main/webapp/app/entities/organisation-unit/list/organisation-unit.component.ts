import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnit } from '../organisation-unit.model';
import { OrganisationUnitService } from '../service/organisation-unit.service';
import { OrganisationUnitDeleteDialogComponent } from '../delete/organisation-unit-delete-dialog.component';

@Component({
  selector: 'app-organisation-unit',
  templateUrl: './organisation-unit.component.html',
})
export class OrganisationUnitComponent implements OnInit {
  organisationUnits?: IOrganisationUnit[];
  isLoading = false;

  constructor(protected organisationUnitService: OrganisationUnitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.organisationUnitService.query().subscribe(
      (res: HttpResponse<IOrganisationUnit[]>) => {
        this.isLoading = false;
        this.organisationUnits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrganisationUnit): number {
    return item.id!;
  }

  delete(organisationUnit: IOrganisationUnit): void {
    const modalRef = this.modalService.open(OrganisationUnitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.organisationUnit = organisationUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
