import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILOB, NewLOB } from '../lob.model';

export type PartialUpdateLOB = Partial<ILOB> & Pick<ILOB, 'id'>;

export type EntityResponseType = HttpResponse<ILOB>;
export type EntityArrayResponseType = HttpResponse<ILOB[]>;

@Injectable({ providedIn: 'root' })
export class LOBService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lobs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lOB: NewLOB): Observable<EntityResponseType> {
    return this.http.post<ILOB>(this.resourceUrl, lOB, { observe: 'response' });
  }

  update(lOB: ILOB): Observable<EntityResponseType> {
    return this.http.put<ILOB>(`${this.resourceUrl}/${this.getLOBIdentifier(lOB)}`, lOB, { observe: 'response' });
  }

  partialUpdate(lOB: PartialUpdateLOB): Observable<EntityResponseType> {
    return this.http.patch<ILOB>(`${this.resourceUrl}/${this.getLOBIdentifier(lOB)}`, lOB, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILOB>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILOB[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLOBIdentifier(lOB: Pick<ILOB, 'id'>): number {
    return lOB.id;
  }

  compareLOB(o1: Pick<ILOB, 'id'> | null, o2: Pick<ILOB, 'id'> | null): boolean {
    return o1 && o2 ? this.getLOBIdentifier(o1) === this.getLOBIdentifier(o2) : o1 === o2;
  }

  addLOBToCollectionIfMissing<Type extends Pick<ILOB, 'id'>>(lOBCollection: Type[], ...lOBSToCheck: (Type | null | undefined)[]): Type[] {
    const lOBS: Type[] = lOBSToCheck.filter(isPresent);
    if (lOBS.length > 0) {
      const lOBCollectionIdentifiers = lOBCollection.map(lOBItem => this.getLOBIdentifier(lOBItem)!);
      const lOBSToAdd = lOBS.filter(lOBItem => {
        const lOBIdentifier = this.getLOBIdentifier(lOBItem);
        if (lOBCollectionIdentifiers.includes(lOBIdentifier)) {
          return false;
        }
        lOBCollectionIdentifiers.push(lOBIdentifier);
        return true;
      });
      return [...lOBSToAdd, ...lOBCollection];
    }
    return lOBCollection;
  }
}
