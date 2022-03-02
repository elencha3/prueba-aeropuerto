import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FlightComponent } from './list/flight.component';
import { FlightDetailComponent } from './detail/flight-detail.component';
import { FlightUpdateComponent } from './update/flight-update.component';
import { FlightDeleteDialogComponent } from './delete/flight-delete-dialog.component';
import { FlightRoutingModule } from './route/flight-routing.module';

@NgModule({
  imports: [SharedModule, FlightRoutingModule],
  declarations: [FlightComponent, FlightDetailComponent, FlightUpdateComponent, FlightDeleteDialogComponent],
  entryComponents: [FlightDeleteDialogComponent],
})
export class FlightModule {}
