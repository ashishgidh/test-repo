import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProgramComponent } from './list/program.component';
import { ProgramDetailComponent } from './detail/program-detail.component';
import { ProgramUpdateComponent } from './update/program-update.component';
import ProgramResolve from './route/program-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const programRoute: Routes = [
  {
    path: '',
    component: ProgramComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProgramDetailComponent,
    resolve: {
      program: ProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProgramUpdateComponent,
    resolve: {
      program: ProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProgramUpdateComponent,
    resolve: {
      program: ProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default programRoute;
