import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFlight, Flight } from '../flight.model';
import { FlightService } from '../service/flight.service';

@Injectable({ providedIn: 'root' })
export class FlightRoutingResolveService implements Resolve<IFlight> {
  constructor(protected service: FlightService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlight> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((flight: HttpResponse<Flight>) => {
          if (flight.body) {
            return of(flight.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Flight());
  }
}
