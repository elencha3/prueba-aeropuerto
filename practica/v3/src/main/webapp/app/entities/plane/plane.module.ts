import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlaneComponent } from './list/plane.component';
import { PlaneDetailComponent } from './detail/plane-detail.component';
import { PlaneUpdateComponent } from './update/plane-update.component';
import { PlaneDeleteDialogComponent } from './delete/plane-delete-dialog.component';
import { PlaneRoutingModule } from './route/plane-routing.module';

@NgModule({
  imports: [SharedModule, PlaneRoutingModule],
  declarations: [PlaneComponent, PlaneDetailComponent, PlaneUpdateComponent, PlaneDeleteDialogComponent],
  entryComponents: [PlaneDeleteDialogComponent],
})
export class PlaneModule {}
