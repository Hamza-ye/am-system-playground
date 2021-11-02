import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganisationUnitLevel } from '../organisation-unit-level.model';
import { OrganisationUnitLevelService } from '../service/organisation-unit-level.service';
import { OrganisationUnitLevelDeleteDialogComponent } from '../delete/organisation-unit-level-delete-dialog.component';

@Component({
  selector: 'app-organisation-unit-level',
  templateUrl: './organisation-unit-level.component.html',
})
export class OrganisationUnitLevelComponent implements OnInit {
  organisationUnitLevels?: IOrganisationUnitLevel[];
  isLoading = false;

  constructor(protected organisationUnitLevelService: OrganisationUnitLevelService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.organisationUnitLevelService.query().subscribe(
      (res: HttpResponse<IOrganisationUnitLevel[]>) => {
        this.isLoading = false;
        this.organisationUnitLevels = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrganisationUnitLevel): number {
    return item.id!;
  }

  delete(organisationUnitLevel: IOrganisationUnitLevel): void {
    const modalRef = this.modalService.open(OrganisationUnitLevelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.organisationUnitLevel = organisationUnitLevel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
