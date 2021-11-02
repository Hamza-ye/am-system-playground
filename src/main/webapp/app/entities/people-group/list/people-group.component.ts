import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeopleGroup } from '../people-group.model';
import { PeopleGroupService } from '../service/people-group.service';
import { PeopleGroupDeleteDialogComponent } from '../delete/people-group-delete-dialog.component';

@Component({
  selector: 'app-people-group',
  templateUrl: './people-group.component.html',
})
export class PeopleGroupComponent implements OnInit {
  peopleGroups?: IPeopleGroup[];
  isLoading = false;

  constructor(protected peopleGroupService: PeopleGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.peopleGroupService.query().subscribe(
      (res: HttpResponse<IPeopleGroup[]>) => {
        this.isLoading = false;
        this.peopleGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPeopleGroup): number {
    return item.id!;
  }

  delete(peopleGroup: IPeopleGroup): void {
    const modalRef = this.modalService.open(PeopleGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.peopleGroup = peopleGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
