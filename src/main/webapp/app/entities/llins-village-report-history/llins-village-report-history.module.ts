import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsVillageReportHistoryComponent } from './list/llins-village-report-history.component';
import { LlinsVillageReportHistoryDetailComponent } from './detail/llins-village-report-history-detail.component';
import { LlinsVillageReportHistoryUpdateComponent } from './update/llins-village-report-history-update.component';
import { LlinsVillageReportHistoryDeleteDialogComponent } from './delete/llins-village-report-history-delete-dialog.component';
import { LlinsVillageReportHistoryRoutingModule } from './route/llins-village-report-history-routing.module';

@NgModule({
  imports: [SharedModule, LlinsVillageReportHistoryRoutingModule],
  declarations: [
    LlinsVillageReportHistoryComponent,
    LlinsVillageReportHistoryDetailComponent,
    LlinsVillageReportHistoryUpdateComponent,
    LlinsVillageReportHistoryDeleteDialogComponent,
  ],
  entryComponents: [LlinsVillageReportHistoryDeleteDialogComponent],
})
export class LlinsVillageReportHistoryModule {}
