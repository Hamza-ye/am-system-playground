import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSFamilyReportHistoryComponent } from './list/llins-family-report-history.component';
import { LLINSFamilyReportHistoryDetailComponent } from './detail/llins-family-report-history-detail.component';
import { LLINSFamilyReportHistoryUpdateComponent } from './update/llins-family-report-history-update.component';
import { LLINSFamilyReportHistoryDeleteDialogComponent } from './delete/llins-family-report-history-delete-dialog.component';
import { LLINSFamilyReportHistoryRoutingModule } from './route/llins-family-report-history-routing.module';

@NgModule({
  imports: [SharedModule, LLINSFamilyReportHistoryRoutingModule],
  declarations: [
    LLINSFamilyReportHistoryComponent,
    LLINSFamilyReportHistoryDetailComponent,
    LLINSFamilyReportHistoryUpdateComponent,
    LLINSFamilyReportHistoryDeleteDialogComponent,
  ],
  entryComponents: [LLINSFamilyReportHistoryDeleteDialogComponent],
})
export class LLINSFamilyReportHistoryModule {}
