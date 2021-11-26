import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsFamilyReportHistoryComponent } from './list/llins-family-report-history.component';
import { LlinsFamilyReportHistoryDetailComponent } from './detail/llins-family-report-history-detail.component';
import { LlinsFamilyReportHistoryUpdateComponent } from './update/llins-family-report-history-update.component';
import { LlinsFamilyReportHistoryDeleteDialogComponent } from './delete/llins-family-report-history-delete-dialog.component';
import { LlinsFamilyReportHistoryRoutingModule } from './route/llins-family-report-history-routing.module';

@NgModule({
  imports: [SharedModule, LlinsFamilyReportHistoryRoutingModule],
  declarations: [
    LlinsFamilyReportHistoryComponent,
    LlinsFamilyReportHistoryDetailComponent,
    LlinsFamilyReportHistoryUpdateComponent,
    LlinsFamilyReportHistoryDeleteDialogComponent,
  ],
  entryComponents: [LlinsFamilyReportHistoryDeleteDialogComponent],
})
export class LlinsFamilyReportHistoryModule {}
