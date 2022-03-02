import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlane, Plane } from '../plane.model';
import { PlaneService } from '../service/plane.service';

@Injectable({ providedIn: 'root' })
export class PlaneRoutingResolveService implements Resolve<IPlane> {
  constructor(protected service: PlaneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlane> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((plane: HttpResponse<Plane>) => {
          if (plane.body) {
            return of(plane.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plane());
  }
}
