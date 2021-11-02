import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MalariaCasesReportComponent } from './list/malaria-cases-report.component';
import { MalariaCasesReportDetailComponent } from './detail/malaria-cases-report-detail.component';
import { MalariaCasesReportUpdateComponent } from './update/malaria-cases-report-update.component';
import { MalariaCasesReportDeleteDialogComponent } from './delete/malaria-cases-report-delete-dialog.component';
import { MalariaCasesReportRoutingModule } from './route/malaria-cases-report-routing.module';

@NgModule({
  imports: [SharedModule, MalariaCasesReportRoutingModule],
  declarations: [
    MalariaCasesReportComponent,
    MalariaCasesReportDetailComponent,
    MalariaCasesReportUpdateComponent,
    MalariaCasesReportDeleteDialogComponent,
  ],
  entryComponents: [MalariaCasesReportDeleteDialogComponent],
})
export class MalariaCasesReportModule {}
