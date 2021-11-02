import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonAuthorityGroupComponent } from './list/person-authority-group.component';
import { PersonAuthorityGroupDetailComponent } from './detail/person-authority-group-detail.component';
import { PersonAuthorityGroupUpdateComponent } from './update/person-authority-group-update.component';
import { PersonAuthorityGroupDeleteDialogComponent } from './delete/person-authority-group-delete-dialog.component';
import { PersonAuthorityGroupRoutingModule } from './route/person-authority-group-routing.module';

@NgModule({
  imports: [SharedModule, PersonAuthorityGroupRoutingModule],
  declarations: [
    PersonAuthorityGroupComponent,
    PersonAuthorityGroupDetailComponent,
    PersonAuthorityGroupUpdateComponent,
    PersonAuthorityGroupDeleteDialogComponent,
  ],
  entryComponents: [PersonAuthorityGroupDeleteDialogComponent],
})
export class PersonAuthorityGroupModule {}
