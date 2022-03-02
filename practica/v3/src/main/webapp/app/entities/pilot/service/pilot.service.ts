import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPilot, getPilotIdentifier } from '../pilot.model';

export type EntityResponseType = HttpResponse<IPilot>;
export type EntityArrayResponseType = HttpResponse<IPilot[]>;

@Injectable({ providedIn: 'root' })
export class PilotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pilots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pilot: IPilot): Observable<EntityResponseType> {
    return this.http.post<IPilot>(this.resourceUrl, pilot, { observe: 'response' });
  }

  update(pilot: IPilot): Observable<EntityResponseType> {
    return this.http.put<IPilot>(`${this.resourceUrl}/${getPilotIdentifier(pilot) as number}`, pilot, { observe: 'response' });
  }

  partialUpdate(pilot: IPilot): Observable<EntityResponseType> {
    return this.http.patch<IPilot>(`${this.resourceUrl}/${getPilotIdentifier(pilot) as number}`, pilot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPilot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPilot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPilotToCollectionIfMissing(pilotCollection: IPilot[], ...pilotsToCheck: (IPilot | null | undefined)[]): IPilot[] {
    const pilots: IPilot[] = pilotsToCheck.filter(isPresent);
    if (pilots.length > 0) {
      const pilotCollectionIdentifiers = pilotCollection.map(pilotItem => getPilotIdentifier(pilotItem)!);
      const pilotsToAdd = pilots.filter(pilotItem => {
        const pilotIdentifier = getPilotIdentifier(pilotItem);
        if (pilotIdentifier == null || pilotCollectionIdentifiers.includes(pilotIdentifier)) {
          return false;
        }
        pilotCollectionIdentifiers.push(pilotIdentifier);
        return true;
      });
      return [...pilotsToAdd, ...pilotCollection];
    }
    return pilotCollection;
  }
}
