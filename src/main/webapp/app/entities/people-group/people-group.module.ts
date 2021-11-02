import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PeopleGroupComponent } from './list/people-group.component';
import { PeopleGroupDetailComponent } from './detail/people-group-detail.component';
import { PeopleGroupUpdateComponent } from './update/people-group-update.component';
import { PeopleGroupDeleteDialogComponent } from './delete/people-group-delete-dialog.component';
import { PeopleGroupRoutingModule } from './route/people-group-routing.module';

@NgModule({
  imports: [SharedModule, PeopleGroupRoutingModule],
  declarations: [PeopleGroupComponent, PeopleGroupDetailComponent, PeopleGroupUpdateComponent, PeopleGroupDeleteDialogComponent],
  entryComponents: [PeopleGroupDeleteDialogComponent],
})
export class PeopleGroupModule {}
