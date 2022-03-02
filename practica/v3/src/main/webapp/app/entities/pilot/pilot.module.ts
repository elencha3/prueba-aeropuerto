import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PilotComponent } from './list/pilot.component';
import { PilotDetailComponent } from './detail/pilot-detail.component';
import { PilotUpdateComponent } from './update/pilot-update.component';
import { PilotDeleteDialogComponent } from './delete/pilot-delete-dialog.component';
import { PilotRoutingModule } from './route/pilot-routing.module';

@NgModule({
  imports: [SharedModule, PilotRoutingModule],
  declarations: [PilotComponent, PilotDetailComponent, PilotUpdateComponent, PilotDeleteDialogComponent],
  entryComponents: [PilotDeleteDialogComponent],
})
export class PilotModule {}
