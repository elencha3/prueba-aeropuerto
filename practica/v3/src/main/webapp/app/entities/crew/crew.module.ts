import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrewComponent } from './list/crew.component';
import { CrewDetailComponent } from './detail/crew-detail.component';
import { CrewUpdateComponent } from './update/crew-update.component';
import { CrewDeleteDialogComponent } from './delete/crew-delete-dialog.component';
import { CrewRoutingModule } from './route/crew-routing.module';

@NgModule({
  imports: [SharedModule, CrewRoutingModule],
  declarations: [CrewComponent, CrewDetailComponent, CrewUpdateComponent, CrewDeleteDialogComponent],
  entryComponents: [CrewDeleteDialogComponent],
})
export class CrewModule {}
