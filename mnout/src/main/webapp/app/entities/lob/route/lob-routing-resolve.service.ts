import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILOB } from '../lob.model';
import { LOBService } from '../service/lob.service';

export const lOBResolve = (route: ActivatedRouteSnapshot): Observable<null | ILOB> => {
  const id = route.params['id'];
  if (id) {
    return inject(LOBService)
      .find(id)
      .pipe(
        mergeMap((lOB: HttpResponse<ILOB>) => {
          if (lOB.body) {
            return of(lOB.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default lOBResolve;
