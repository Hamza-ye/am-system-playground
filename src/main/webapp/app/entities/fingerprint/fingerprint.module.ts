import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FingerprintComponent } from './list/fingerprint.component';
import { FingerprintDetailComponent } from './detail/fingerprint-detail.component';
import { FingerprintUpdateComponent } from './update/fingerprint-update.component';
import { FingerprintDeleteDialogComponent } from './delete/fingerprint-delete-dialog.component';
import { FingerprintRoutingModule } from './route/fingerprint-routing.module';

@NgModule({
  imports: [SharedModule, FingerprintRoutingModule],
  declarations: [FingerprintComponent, FingerprintDetailComponent, FingerprintUpdateComponent, FingerprintDeleteDialogComponent],
  entryComponents: [FingerprintDeleteDialogComponent],
})
export class FingerprintModule {}
