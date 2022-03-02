import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFlight, getFlightIdentifier } from '../flight.model';

export type EntityResponseType = HttpResponse<IFlight>;
export type EntityArrayResponseType = HttpResponse<IFlight[]>;

@Injectable({ providedIn: 'root' })
export class FlightService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/flights');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(flight: IFlight): Observable<EntityResponseType> {
    return this.http.post<IFlight>(this.resourceUrl, flight, { observe: 'response' });
  }

  update(flight: IFlight): Observable<EntityResponseType> {
    return this.http.put<IFlight>(`${this.resourceUrl}/${getFlightIdentifier(flight) as number}`, flight, { observe: 'response' });
  }

  partialUpdate(flight: IFlight): Observable<EntityResponseType> {
    return this.http.patch<IFlight>(`${this.resourceUrl}/${getFlightIdentifier(flight) as number}`, flight, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFlight>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFlight[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFlightToCollectionIfMissing(flightCollection: IFlight[], ...flightsToCheck: (IFlight | null | undefined)[]): IFlight[] {
    const flights: IFlight[] = flightsToCheck.filter(isPresent);
    if (flights.length > 0) {
      const flightCollectionIdentifiers = flightCollection.map(flightItem => getFlightIdentifier(flightItem)!);
      const flightsToAdd = flights.filter(flightItem => {
        const flightIdentifier = getFlightIdentifier(flightItem);
        if (flightIdentifier == null || flightCollectionIdentifiers.includes(flightIdentifier)) {
          return false;
        }
        flightCollectionIdentifiers.push(flightIdentifier);
        return true;
      });
      return [...flightsToAdd, ...flightCollection];
    }
    return flightCollection;
  }
}
