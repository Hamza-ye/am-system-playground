import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChvMalariaCaseReportComponent } from './list/chv-malaria-case-report.component';
import { ChvMalariaCaseReportDetailComponent } from './detail/chv-malaria-case-report-detail.component';
import { ChvMalariaCaseReportUpdateComponent } from './update/chv-malaria-case-report-update.component';
import { ChvMalariaCaseReportDeleteDialogComponent } from './delete/chv-malaria-case-report-delete-dialog.component';
import { ChvMalariaCaseReportRoutingModule } from './route/chv-malaria-case-report-routing.module';

@NgModule({
  imports: [SharedModule, ChvMalariaCaseReportRoutingModule],
  declarations: [
    ChvMalariaCaseReportComponent,
    ChvMalariaCaseReportDetailComponent,
    ChvMalariaCaseReportUpdateComponent,
    ChvMalariaCaseReportDeleteDialogComponent,
  ],
  entryComponents: [ChvMalariaCaseReportDeleteDialogComponent],
})
export class ChvMalariaCaseReportModule {}
