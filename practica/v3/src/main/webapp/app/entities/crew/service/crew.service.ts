import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICrew, getCrewIdentifier } from '../crew.model';

export type EntityResponseType = HttpResponse<ICrew>;
export type EntityArrayResponseType = HttpResponse<ICrew[]>;

@Injectable({ providedIn: 'root' })
export class CrewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crews');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crew: ICrew): Observable<EntityResponseType> {
    return this.http.post<ICrew>(this.resourceUrl, crew, { observe: 'response' });
  }

  update(crew: ICrew): Observable<EntityResponseType> {
    return this.http.put<ICrew>(`${this.resourceUrl}/${getCrewIdentifier(crew) as number}`, crew, { observe: 'response' });
  }

  partialUpdate(crew: ICrew): Observable<EntityResponseType> {
    return this.http.patch<ICrew>(`${this.resourceUrl}/${getCrewIdentifier(crew) as number}`, crew, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrew>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrew[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCrewToCollectionIfMissing(crewCollection: ICrew[], ...crewsToCheck: (ICrew | null | undefined)[]): ICrew[] {
    const crews: ICrew[] = crewsToCheck.filter(isPresent);
    if (crews.length > 0) {
      const crewCollectionIdentifiers = crewCollection.map(crewItem => getCrewIdentifier(crewItem)!);
      const crewsToAdd = crews.filter(crewItem => {
        const crewIdentifier = getCrewIdentifier(crewItem);
        if (crewIdentifier == null || crewCollectionIdentifiers.includes(crewIdentifier)) {
          return false;
        }
        crewCollectionIdentifiers.push(crewIdentifier);
        return true;
      });
      return [...crewsToAdd, ...crewCollection];
    }
    return crewCollection;
  }
}
