import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPortfolioSegment } from '../portfolio-segment.model';
import { PortfolioSegmentService } from '../service/portfolio-segment.service';

export const portfolioSegmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IPortfolioSegment> => {
  const id = route.params['id'];
  if (id) {
    return inject(PortfolioSegmentService)
      .find(id)
      .pipe(
        mergeMap((portfolioSegment: HttpResponse<IPortfolioSegment>) => {
          if (portfolioSegment.body) {
            return of(portfolioSegment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default portfolioSegmentResolve;
