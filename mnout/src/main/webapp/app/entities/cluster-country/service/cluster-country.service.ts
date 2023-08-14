import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClusterCountry, NewClusterCountry } from '../cluster-country.model';

export type PartialUpdateClusterCountry = Partial<IClusterCountry> & Pick<IClusterCountry, 'id'>;

export type EntityResponseType = HttpResponse<IClusterCountry>;
export type EntityArrayResponseType = HttpResponse<IClusterCountry[]>;

@Injectable({ providedIn: 'root' })
export class ClusterCountryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cluster-countries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clusterCountry: NewClusterCountry): Observable<EntityResponseType> {
    return this.http.post<IClusterCountry>(this.resourceUrl, clusterCountry, { observe: 'response' });
  }

  update(clusterCountry: IClusterCountry): Observable<EntityResponseType> {
    return this.http.put<IClusterCountry>(`${this.resourceUrl}/${this.getClusterCountryIdentifier(clusterCountry)}`, clusterCountry, {
      observe: 'response',
    });
  }

  partialUpdate(clusterCountry: PartialUpdateClusterCountry): Observable<EntityResponseType> {
    return this.http.patch<IClusterCountry>(`${this.resourceUrl}/${this.getClusterCountryIdentifier(clusterCountry)}`, clusterCountry, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClusterCountry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClusterCountry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClusterCountryIdentifier(clusterCountry: Pick<IClusterCountry, 'id'>): number {
    return clusterCountry.id;
  }

  compareClusterCountry(o1: Pick<IClusterCountry, 'id'> | null, o2: Pick<IClusterCountry, 'id'> | null): boolean {
    return o1 && o2 ? this.getClusterCountryIdentifier(o1) === this.getClusterCountryIdentifier(o2) : o1 === o2;
  }

  addClusterCountryToCollectionIfMissing<Type extends Pick<IClusterCountry, 'id'>>(
    clusterCountryCollection: Type[],
    ...clusterCountriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clusterCountries: Type[] = clusterCountriesToCheck.filter(isPresent);
    if (clusterCountries.length > 0) {
      const clusterCountryCollectionIdentifiers = clusterCountryCollection.map(
        clusterCountryItem => this.getClusterCountryIdentifier(clusterCountryItem)!
      );
      const clusterCountriesToAdd = clusterCountries.filter(clusterCountryItem => {
        const clusterCountryIdentifier = this.getClusterCountryIdentifier(clusterCountryItem);
        if (clusterCountryCollectionIdentifiers.includes(clusterCountryIdentifier)) {
          return false;
        }
        clusterCountryCollectionIdentifiers.push(clusterCountryIdentifier);
        return true;
      });
      return [...clusterCountriesToAdd, ...clusterCountryCollection];
    }
    return clusterCountryCollection;
  }
}
