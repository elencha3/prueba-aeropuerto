import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAirport, getAirportIdentifier } from '../airport.model';

export type EntityResponseType = HttpResponse<IAirport>;
export type EntityArrayResponseType = HttpResponse<IAirport[]>;

@Injectable({ providedIn: 'root' })
export class AirportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/airports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(airport: IAirport): Observable<EntityResponseType> {
    return this.http.post<IAirport>(this.resourceUrl, airport, { observe: 'response' });
  }

  update(airport: IAirport): Observable<EntityResponseType> {
    return this.http.put<IAirport>(`${this.resourceUrl}/${getAirportIdentifier(airport) as number}`, airport, { observe: 'response' });
  }

  partialUpdate(airport: IAirport): Observable<EntityResponseType> {
    return this.http.patch<IAirport>(`${this.resourceUrl}/${getAirportIdentifier(airport) as number}`, airport, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAirport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAirport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAirportToCollectionIfMissing(airportCollection: IAirport[], ...airportsToCheck: (IAirport | null | undefined)[]): IAirport[] {
    const airports: IAirport[] = airportsToCheck.filter(isPresent);
    if (airports.length > 0) {
      const airportCollectionIdentifiers = airportCollection.map(airportItem => getAirportIdentifier(airportItem)!);
      const airportsToAdd = airports.filter(airportItem => {
        const airportIdentifier = getAirportIdentifier(airportItem);
        if (airportIdentifier == null || airportCollectionIdentifiers.includes(airportIdentifier)) {
          return false;
        }
        airportCollectionIdentifiers.push(airportIdentifier);
        return true;
      });
      return [...airportsToAdd, ...airportCollection];
    }
    return airportCollection;
  }
}
