import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClusterCountry } from '../cluster-country.model';
import { ClusterCountryService } from '../service/cluster-country.service';

export const clusterCountryResolve = (route: ActivatedRouteSnapshot): Observable<null | IClusterCountry> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClusterCountryService)
      .find(id)
      .pipe(
        mergeMap((clusterCountry: HttpResponse<IClusterCountry>) => {
          if (clusterCountry.body) {
            return of(clusterCountry.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default clusterCountryResolve;
