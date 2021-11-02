import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonAuthorityGroup } from '../person-authority-group.model';
import { PersonAuthorityGroupService } from '../service/person-authority-group.service';
import { PersonAuthorityGroupDeleteDialogComponent } from '../delete/person-authority-group-delete-dialog.component';

@Component({
  selector: 'app-person-authority-group',
  templateUrl: './person-authority-group.component.html',
})
export class PersonAuthorityGroupComponent implements OnInit {
  personAuthorityGroups?: IPersonAuthorityGroup[];
  isLoading = false;

  constructor(protected personAuthorityGroupService: PersonAuthorityGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personAuthorityGroupService.query().subscribe(
      (res: HttpResponse<IPersonAuthorityGroup[]>) => {
        this.isLoading = false;
        this.personAuthorityGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonAuthorityGroup): number {
    return item.id!;
  }

  delete(personAuthorityGroup: IPersonAuthorityGroup): void {
    const modalRef = this.modalService.open(PersonAuthorityGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personAuthorityGroup = personAuthorityGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
