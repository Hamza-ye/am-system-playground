import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSVillageReportHistoryComponent } from './list/llins-village-report-history.component';
import { LLINSVillageReportHistoryDetailComponent } from './detail/llins-village-report-history-detail.component';
import { LLINSVillageReportHistoryUpdateComponent } from './update/llins-village-report-history-update.component';
import { LLINSVillageReportHistoryDeleteDialogComponent } from './delete/llins-village-report-history-delete-dialog.component';
import { LLINSVillageReportHistoryRoutingModule } from './route/llins-village-report-history-routing.module';

@NgModule({
  imports: [SharedModule, LLINSVillageReportHistoryRoutingModule],
  declarations: [
    LLINSVillageReportHistoryComponent,
    LLINSVillageReportHistoryDetailComponent,
    LLINSVillageReportHistoryUpdateComponent,
    LLINSVillageReportHistoryDeleteDialogComponent,
  ],
  entryComponents: [LLINSVillageReportHistoryDeleteDialogComponent],
})
export class LLINSVillageReportHistoryModule {}
