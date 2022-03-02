import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AirportComponent } from './list/airport.component';
import { AirportDetailComponent } from './detail/airport-detail.component';
import { AirportUpdateComponent } from './update/airport-update.component';
import { AirportDeleteDialogComponent } from './delete/airport-delete-dialog.component';
import { AirportRoutingModule } from './route/airport-routing.module';

@NgModule({
  imports: [SharedModule, AirportRoutingModule],
  declarations: [AirportComponent, AirportDetailComponent, AirportUpdateComponent, AirportDeleteDialogComponent],
  entryComponents: [AirportDeleteDialogComponent],
})
export class AirportModule {}
