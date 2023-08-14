import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProgram } from '../program.model';
import { ProgramService } from '../service/program.service';

export const programResolve = (route: ActivatedRouteSnapshot): Observable<null | IProgram> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProgramService)
      .find(id)
      .pipe(
        mergeMap((program: HttpResponse<IProgram>) => {
          if (program.body) {
            return of(program.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default programResolve;
