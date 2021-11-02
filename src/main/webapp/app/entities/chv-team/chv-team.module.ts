import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CHVTeamComponent } from './list/chv-team.component';
import { CHVTeamDetailComponent } from './detail/chv-team-detail.component';
import { CHVTeamUpdateComponent } from './update/chv-team-update.component';
import { CHVTeamDeleteDialogComponent } from './delete/chv-team-delete-dialog.component';
import { CHVTeamRoutingModule } from './route/chv-team-routing.module';

@NgModule({
  imports: [SharedModule, CHVTeamRoutingModule],
  declarations: [CHVTeamComponent, CHVTeamDetailComponent, CHVTeamUpdateComponent, CHVTeamDeleteDialogComponent],
  entryComponents: [CHVTeamDeleteDialogComponent],
})
export class CHVTeamModule {}
