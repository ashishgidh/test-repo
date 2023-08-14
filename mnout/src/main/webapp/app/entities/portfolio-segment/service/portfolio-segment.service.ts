import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPortfolioSegment, NewPortfolioSegment } from '../portfolio-segment.model';

export type PartialUpdatePortfolioSegment = Partial<IPortfolioSegment> & Pick<IPortfolioSegment, 'id'>;

export type EntityResponseType = HttpResponse<IPortfolioSegment>;
export type EntityArrayResponseType = HttpResponse<IPortfolioSegment[]>;

@Injectable({ providedIn: 'root' })
export class PortfolioSegmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/portfolio-segments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(portfolioSegment: NewPortfolioSegment): Observable<EntityResponseType> {
    return this.http.post<IPortfolioSegment>(this.resourceUrl, portfolioSegment, { observe: 'response' });
  }

  update(portfolioSegment: IPortfolioSegment): Observable<EntityResponseType> {
    return this.http.put<IPortfolioSegment>(
      `${this.resourceUrl}/${this.getPortfolioSegmentIdentifier(portfolioSegment)}`,
      portfolioSegment,
      { observe: 'response' }
    );
  }

  partialUpdate(portfolioSegment: PartialUpdatePortfolioSegment): Observable<EntityResponseType> {
    return this.http.patch<IPortfolioSegment>(
      `${this.resourceUrl}/${this.getPortfolioSegmentIdentifier(portfolioSegment)}`,
      portfolioSegment,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPortfolioSegment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPortfolioSegment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPortfolioSegmentIdentifier(portfolioSegment: Pick<IPortfolioSegment, 'id'>): string {
    return portfolioSegment.id;
  }

  comparePortfolioSegment(o1: Pick<IPortfolioSegment, 'id'> | null, o2: Pick<IPortfolioSegment, 'id'> | null): boolean {
    return o1 && o2 ? this.getPortfolioSegmentIdentifier(o1) === this.getPortfolioSegmentIdentifier(o2) : o1 === o2;
  }

  addPortfolioSegmentToCollectionIfMissing<Type extends Pick<IPortfolioSegment, 'id'>>(
    portfolioSegmentCollection: Type[],
    ...portfolioSegmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const portfolioSegments: Type[] = portfolioSegmentsToCheck.filter(isPresent);
    if (portfolioSegments.length > 0) {
      const portfolioSegmentCollectionIdentifiers = portfolioSegmentCollection.map(
        portfolioSegmentItem => this.getPortfolioSegmentIdentifier(portfolioSegmentItem)!
      );
      const portfolioSegmentsToAdd = portfolioSegments.filter(portfolioSegmentItem => {
        const portfolioSegmentIdentifier = this.getPortfolioSegmentIdentifier(portfolioSegmentItem);
        if (portfolioSegmentCollectionIdentifiers.includes(portfolioSegmentIdentifier)) {
          return false;
        }
        portfolioSegmentCollectionIdentifiers.push(portfolioSegmentIdentifier);
        return true;
      });
      return [...portfolioSegmentsToAdd, ...portfolioSegmentCollection];
    }
    return portfolioSegmentCollection;
  }
}
