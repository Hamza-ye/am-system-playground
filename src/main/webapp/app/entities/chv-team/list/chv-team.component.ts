import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChvTeam } from '../chv-team.model';
import { ChvTeamService } from '../service/chv-team.service';
import { ChvTeamDeleteDialogComponent } from '../delete/chv-team-delete-dialog.component';

@Component({
  selector: 'app-chv-team',
  templateUrl: './chv-team.component.html',
})
export class ChvTeamComponent implements OnInit {
  chvTeams?: IChvTeam[];
  isLoading = false;

  constructor(protected chvTeamService: ChvTeamService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.chvTeamService.query().subscribe(
      (res: HttpResponse<IChvTeam[]>) => {
        this.isLoading = false;
        this.chvTeams = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IChvTeam): number {
    return item.id!;
  }

  delete(chvTeam: IChvTeam): void {
    const modalRef = this.modalService.open(ChvTeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chvTeam = chvTeam;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
