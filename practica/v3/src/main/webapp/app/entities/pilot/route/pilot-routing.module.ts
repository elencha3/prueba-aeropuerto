import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PilotComponent } from '../list/pilot.component';
import { PilotDetailComponent } from '../detail/pilot-detail.component';
import { PilotUpdateComponent } from '../update/pilot-update.component';
import { PilotRoutingResolveService } from './pilot-routing-resolve.service';

const pilotRoute: Routes = [
  {
    path: '',
    component: PilotComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PilotDetailComponent,
    resolve: {
      pilot: PilotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PilotUpdateComponent,
    resolve: {
      pilot: PilotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PilotUpdateComponent,
    resolve: {
      pilot: PilotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pilotRoute)],
  exports: [RouterModule],
})
export class PilotRoutingModule {}
