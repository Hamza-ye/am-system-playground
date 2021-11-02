import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StatusOfCoverageComponent } from './list/status-of-coverage.component';
import { StatusOfCoverageDetailComponent } from './detail/status-of-coverage-detail.component';
import { StatusOfCoverageUpdateComponent } from './update/status-of-coverage-update.component';
import { StatusOfCoverageDeleteDialogComponent } from './delete/status-of-coverage-delete-dialog.component';
import { StatusOfCoverageRoutingModule } from './route/status-of-coverage-routing.module';

@NgModule({
  imports: [SharedModule, StatusOfCoverageRoutingModule],
  declarations: [
    StatusOfCoverageComponent,
    StatusOfCoverageDetailComponent,
    StatusOfCoverageUpdateComponent,
    StatusOfCoverageDeleteDialogComponent,
  ],
  entryComponents: [StatusOfCoverageDeleteDialogComponent],
})
export class StatusOfCoverageModule {}
