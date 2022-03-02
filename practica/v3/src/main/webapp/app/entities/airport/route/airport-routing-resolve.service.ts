import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAirport, Airport } from '../airport.model';
import { AirportService } from '../service/airport.service';

@Injectable({ providedIn: 'root' })
export class AirportRoutingResolveService implements Resolve<IAirport> {
  constructor(protected service: AirportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAirport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((airport: HttpResponse<Airport>) => {
          if (airport.body) {
            return of(airport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Airport());
  }
}
