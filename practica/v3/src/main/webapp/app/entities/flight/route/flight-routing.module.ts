import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FlightComponent } from '../list/flight.component';
import { FlightDetailComponent } from '../detail/flight-detail.component';
import { FlightUpdateComponent } from '../update/flight-update.component';
import { FlightRoutingResolveService } from './flight-routing-resolve.service';

const flightRoute: Routes = [
  {
    path: '',
    component: FlightComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlightDetailComponent,
    resolve: {
      flight: FlightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlightUpdateComponent,
    resolve: {
      flight: FlightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlightUpdateComponent,
    resolve: {
      flight: FlightRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(flightRoute)],
  exports: [RouterModule],
})
export class FlightRoutingModule {}
