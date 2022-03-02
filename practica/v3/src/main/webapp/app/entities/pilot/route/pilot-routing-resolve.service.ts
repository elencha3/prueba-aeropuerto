import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPilot, Pilot } from '../pilot.model';
import { PilotService } from '../service/pilot.service';

@Injectable({ providedIn: 'root' })
export class PilotRoutingResolveService implements Resolve<IPilot> {
  constructor(protected service: PilotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPilot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pilot: HttpResponse<Pilot>) => {
          if (pilot.body) {
            return of(pilot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pilot());
  }
}
