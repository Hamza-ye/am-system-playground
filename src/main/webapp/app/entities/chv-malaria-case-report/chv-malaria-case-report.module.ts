import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CHVMalariaCaseReportComponent } from './list/chv-malaria-case-report.component';
import { CHVMalariaCaseReportDetailComponent } from './detail/chv-malaria-case-report-detail.component';
import { CHVMalariaCaseReportUpdateComponent } from './update/chv-malaria-case-report-update.component';
import { CHVMalariaCaseReportDeleteDialogComponent } from './delete/chv-malaria-case-report-delete-dialog.component';
import { CHVMalariaCaseReportRoutingModule } from './route/chv-malaria-case-report-routing.module';

@NgModule({
  imports: [SharedModule, CHVMalariaCaseReportRoutingModule],
  declarations: [
    CHVMalariaCaseReportComponent,
    CHVMalariaCaseReportDetailComponent,
    CHVMalariaCaseReportUpdateComponent,
    CHVMalariaCaseReportDeleteDialogComponent,
  ],
  entryComponents: [CHVMalariaCaseReportDeleteDialogComponent],
})
export class CHVMalariaCaseReportModule {}
