import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrew, Crew } from '../crew.model';
import { CrewService } from '../service/crew.service';

@Injectable({ providedIn: 'root' })
export class CrewRoutingResolveService implements Resolve<ICrew> {
  constructor(protected service: CrewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrew> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crew: HttpResponse<Crew>) => {
          if (crew.body) {
            return of(crew.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Crew());
  }
}
