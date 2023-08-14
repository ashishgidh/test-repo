import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICSEUser, NewCSEUser } from '../cse-user.model';

export type PartialUpdateCSEUser = Partial<ICSEUser> & Pick<ICSEUser, 'id'>;

export type EntityResponseType = HttpResponse<ICSEUser>;
export type EntityArrayResponseType = HttpResponse<ICSEUser[]>;

@Injectable({ providedIn: 'root' })
export class CSEUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cse-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cSEUser: NewCSEUser): Observable<EntityResponseType> {
    return this.http.post<ICSEUser>(this.resourceUrl, cSEUser, { observe: 'response' });
  }

  update(cSEUser: ICSEUser): Observable<EntityResponseType> {
    return this.http.put<ICSEUser>(`${this.resourceUrl}/${this.getCSEUserIdentifier(cSEUser)}`, cSEUser, { observe: 'response' });
  }

  partialUpdate(cSEUser: PartialUpdateCSEUser): Observable<EntityResponseType> {
    return this.http.patch<ICSEUser>(`${this.resourceUrl}/${this.getCSEUserIdentifier(cSEUser)}`, cSEUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICSEUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICSEUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCSEUserIdentifier(cSEUser: Pick<ICSEUser, 'id'>): number {
    return cSEUser.id;
  }

  compareCSEUser(o1: Pick<ICSEUser, 'id'> | null, o2: Pick<ICSEUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getCSEUserIdentifier(o1) === this.getCSEUserIdentifier(o2) : o1 === o2;
  }

  addCSEUserToCollectionIfMissing<Type extends Pick<ICSEUser, 'id'>>(
    cSEUserCollection: Type[],
    ...cSEUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cSEUsers: Type[] = cSEUsersToCheck.filter(isPresent);
    if (cSEUsers.length > 0) {
      const cSEUserCollectionIdentifiers = cSEUserCollection.map(cSEUserItem => this.getCSEUserIdentifier(cSEUserItem)!);
      const cSEUsersToAdd = cSEUsers.filter(cSEUserItem => {
        const cSEUserIdentifier = this.getCSEUserIdentifier(cSEUserItem);
        if (cSEUserCollectionIdentifiers.includes(cSEUserIdentifier)) {
          return false;
        }
        cSEUserCollectionIdentifiers.push(cSEUserIdentifier);
        return true;
      });
      return [...cSEUsersToAdd, ...cSEUserCollection];
    }
    return cSEUserCollection;
  }
}
