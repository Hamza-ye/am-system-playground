import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsFamilyReportComponent } from './list/llins-family-report.component';
import { LlinsFamilyReportDetailComponent } from './detail/llins-family-report-detail.component';
import { LlinsFamilyReportUpdateComponent } from './update/llins-family-report-update.component';
import { LlinsFamilyReportDeleteDialogComponent } from './delete/llins-family-report-delete-dialog.component';
import { LlinsFamilyReportRoutingModule } from './route/llins-family-report-routing.module';

@NgModule({
  imports: [SharedModule, LlinsFamilyReportRoutingModule],
  declarations: [
    LlinsFamilyReportComponent,
    LlinsFamilyReportDetailComponent,
    LlinsFamilyReportUpdateComponent,
    LlinsFamilyReportDeleteDialogComponent,
  ],
  entryComponents: [LlinsFamilyReportDeleteDialogComponent],
})
export class LlinsFamilyReportModule {}
