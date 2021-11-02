import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkingDayComponent } from './list/working-day.component';
import { WorkingDayDetailComponent } from './detail/working-day-detail.component';
import { WorkingDayUpdateComponent } from './update/working-day-update.component';
import { WorkingDayDeleteDialogComponent } from './delete/working-day-delete-dialog.component';
import { WorkingDayRoutingModule } from './route/working-day-routing.module';

@NgModule({
  imports: [SharedModule, WorkingDayRoutingModule],
  declarations: [WorkingDayComponent, WorkingDayDetailComponent, WorkingDayUpdateComponent, WorkingDayDeleteDialogComponent],
  entryComponents: [WorkingDayDeleteDialogComponent],
})
export class WorkingDayModule {}
