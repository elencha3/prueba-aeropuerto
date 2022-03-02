import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlaneComponent } from '../list/plane.component';
import { PlaneDetailComponent } from '../detail/plane-detail.component';
import { PlaneUpdateComponent } from '../update/plane-update.component';
import { PlaneRoutingResolveService } from './plane-routing-resolve.service';

const planeRoute: Routes = [
  {
    path: '',
    component: PlaneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaneDetailComponent,
    resolve: {
      plane: PlaneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaneUpdateComponent,
    resolve: {
      plane: PlaneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaneUpdateComponent,
    resolve: {
      plane: PlaneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(planeRoute)],
  exports: [RouterModule],
})
export class PlaneRoutingModule {}
