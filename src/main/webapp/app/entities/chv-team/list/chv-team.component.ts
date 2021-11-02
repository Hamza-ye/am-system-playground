import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVTeam } from '../chv-team.model';
import { CHVTeamService } from '../service/chv-team.service';
import { CHVTeamDeleteDialogComponent } from '../delete/chv-team-delete-dialog.component';

@Component({
  selector: 'app-chv-team',
  templateUrl: './chv-team.component.html',
})
export class CHVTeamComponent implements OnInit {
  cHVTeams?: ICHVTeam[];
  isLoading = false;

  constructor(protected cHVTeamService: CHVTeamService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cHVTeamService.query().subscribe(
      (res: HttpResponse<ICHVTeam[]>) => {
        this.isLoading = false;
        this.cHVTeams = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICHVTeam): number {
    return item.id!;
  }

  delete(cHVTeam: ICHVTeam): void {
    const modalRef = this.modalService.open(CHVTeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cHVTeam = cHVTeam;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
