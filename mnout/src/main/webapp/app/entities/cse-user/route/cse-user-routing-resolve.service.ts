import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICSEUser } from '../cse-user.model';
import { CSEUserService } from '../service/cse-user.service';

export const cSEUserResolve = (route: ActivatedRouteSnapshot): Observable<null | ICSEUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(CSEUserService)
      .find(id)
      .pipe(
        mergeMap((cSEUser: HttpResponse<ICSEUser>) => {
          if (cSEUser.body) {
            return of(cSEUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default cSEUserResolve;
