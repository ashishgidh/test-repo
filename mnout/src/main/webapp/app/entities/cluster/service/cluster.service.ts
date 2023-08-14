import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICluster, NewCluster } from '../cluster.model';

export type PartialUpdateCluster = Partial<ICluster> & Pick<ICluster, 'id'>;

export type EntityResponseType = HttpResponse<ICluster>;
export type EntityArrayResponseType = HttpResponse<ICluster[]>;

@Injectable({ providedIn: 'root' })
export class ClusterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clusters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cluster: NewCluster): Observable<EntityResponseType> {
    return this.http.post<ICluster>(this.resourceUrl, cluster, { observe: 'response' });
  }

  update(cluster: ICluster): Observable<EntityResponseType> {
    return this.http.put<ICluster>(`${this.resourceUrl}/${this.getClusterIdentifier(cluster)}`, cluster, { observe: 'response' });
  }

  partialUpdate(cluster: PartialUpdateCluster): Observable<EntityResponseType> {
    return this.http.patch<ICluster>(`${this.resourceUrl}/${this.getClusterIdentifier(cluster)}`, cluster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICluster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICluster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClusterIdentifier(cluster: Pick<ICluster, 'id'>): number {
    return cluster.id;
  }

  compareCluster(o1: Pick<ICluster, 'id'> | null, o2: Pick<ICluster, 'id'> | null): boolean {
    return o1 && o2 ? this.getClusterIdentifier(o1) === this.getClusterIdentifier(o2) : o1 === o2;
  }

  addClusterToCollectionIfMissing<Type extends Pick<ICluster, 'id'>>(
    clusterCollection: Type[],
    ...clustersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clusters: Type[] = clustersToCheck.filter(isPresent);
    if (clusters.length > 0) {
      const clusterCollectionIdentifiers = clusterCollection.map(clusterItem => this.getClusterIdentifier(clusterItem)!);
      const clustersToAdd = clusters.filter(clusterItem => {
        const clusterIdentifier = this.getClusterIdentifier(clusterItem);
        if (clusterCollectionIdentifiers.includes(clusterIdentifier)) {
          return false;
        }
        clusterCollectionIdentifiers.push(clusterIdentifier);
        return true;
      });
      return [...clustersToAdd, ...clusterCollection];
    }
    return clusterCollection;
  }
}
