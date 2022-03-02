import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrewComponent } from '../list/crew.component';
import { CrewDetailComponent } from '../detail/crew-detail.component';
import { CrewUpdateComponent } from '../update/crew-update.component';
import { CrewRoutingResolveService } from './crew-routing-resolve.service';

const crewRoute: Routes = [
  {
    path: '',
    component: CrewComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrewDetailComponent,
    resolve: {
      crew: CrewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrewUpdateComponent,
    resolve: {
      crew: CrewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrewUpdateComponent,
    resolve: {
      crew: CrewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crewRoute)],
  exports: [RouterModule],
})
export class CrewRoutingModule {}
