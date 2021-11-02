import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CHVMalariaReportVersion1Component } from './list/chv-malaria-report-version-1.component';
import { CHVMalariaReportVersion1DetailComponent } from './detail/chv-malaria-report-version-1-detail.component';
import { CHVMalariaReportVersion1UpdateComponent } from './update/chv-malaria-report-version-1-update.component';
import { CHVMalariaReportVersion1DeleteDialogComponent } from './delete/chv-malaria-report-version-1-delete-dialog.component';
import { CHVMalariaReportVersion1RoutingModule } from './route/chv-malaria-report-version-1-routing.module';

@NgModule({
  imports: [SharedModule, CHVMalariaReportVersion1RoutingModule],
  declarations: [
    CHVMalariaReportVersion1Component,
    CHVMalariaReportVersion1DetailComponent,
    CHVMalariaReportVersion1UpdateComponent,
    CHVMalariaReportVersion1DeleteDialogComponent,
  ],
  entryComponents: [CHVMalariaReportVersion1DeleteDialogComponent],
})
export class CHVMalariaReportVersion1Module {}
