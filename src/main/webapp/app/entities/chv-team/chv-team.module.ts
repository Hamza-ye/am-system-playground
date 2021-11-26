import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChvTeamComponent } from './list/chv-team.component';
import { ChvTeamDetailComponent } from './detail/chv-team-detail.component';
import { ChvTeamUpdateComponent } from './update/chv-team-update.component';
import { ChvTeamDeleteDialogComponent } from './delete/chv-team-delete-dialog.component';
import { ChvTeamRoutingModule } from './route/chv-team-routing.module';

@NgModule({
  imports: [SharedModule, ChvTeamRoutingModule],
  declarations: [ChvTeamComponent, ChvTeamDetailComponent, ChvTeamUpdateComponent, ChvTeamDeleteDialogComponent],
  entryComponents: [ChvTeamDeleteDialogComponent],
})
export class ChvTeamModule {}
