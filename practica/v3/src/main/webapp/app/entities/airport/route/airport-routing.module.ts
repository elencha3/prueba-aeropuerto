import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AirportComponent } from '../list/airport.component';
import { AirportDetailComponent } from '../detail/airport-detail.component';
import { AirportUpdateComponent } from '../update/airport-update.component';
import { AirportRoutingResolveService } from './airport-routing-resolve.service';

const airportRoute: Routes = [
  {
    path: '',
    component: AirportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AirportDetailComponent,
    resolve: {
      airport: AirportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AirportUpdateComponent,
    resolve: {
      airport: AirportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AirportUpdateComponent,
    resolve: {
      airport: AirportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(airportRoute)],
  exports: [RouterModule],
})
export class AirportRoutingModule {}
