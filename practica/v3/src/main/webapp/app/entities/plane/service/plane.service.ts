import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlane, getPlaneIdentifier } from '../plane.model';

export type EntityResponseType = HttpResponse<IPlane>;
export type EntityArrayResponseType = HttpResponse<IPlane[]>;

@Injectable({ providedIn: 'root' })
export class PlaneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/planes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(plane: IPlane): Observable<EntityResponseType> {
    return this.http.post<IPlane>(this.resourceUrl, plane, { observe: 'response' });
  }

  update(plane: IPlane): Observable<EntityResponseType> {
    return this.http.put<IPlane>(`${this.resourceUrl}/${getPlaneIdentifier(plane) as number}`, plane, { observe: 'response' });
  }

  partialUpdate(plane: IPlane): Observable<EntityResponseType> {
    return this.http.patch<IPlane>(`${this.resourceUrl}/${getPlaneIdentifier(plane) as number}`, plane, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlane>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlane[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlaneToCollectionIfMissing(planeCollection: IPlane[], ...planesToCheck: (IPlane | null | undefined)[]): IPlane[] {
    const planes: IPlane[] = planesToCheck.filter(isPresent);
    if (planes.length > 0) {
      const planeCollectionIdentifiers = planeCollection.map(planeItem => getPlaneIdentifier(planeItem)!);
      const planesToAdd = planes.filter(planeItem => {
        const planeIdentifier = getPlaneIdentifier(planeItem);
        if (planeIdentifier == null || planeCollectionIdentifiers.includes(planeIdentifier)) {
          return false;
        }
        planeCollectionIdentifiers.push(planeIdentifier);
        return true;
      });
      return [...planesToAdd, ...planeCollection];
    }
    return planeCollection;
  }
}
