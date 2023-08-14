import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CSEUserComponent } from './list/cse-user.component';
import { CSEUserDetailComponent } from './detail/cse-user-detail.component';
import { CSEUserUpdateComponent } from './update/cse-user-update.component';
import CSEUserResolve from './route/cse-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cSEUserRoute: Routes = [
  {
    path: '',
    component: CSEUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CSEUserDetailComponent,
    resolve: {
      cSEUser: CSEUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CSEUserUpdateComponent,
    resolve: {
      cSEUser: CSEUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CSEUserUpdateComponent,
    resolve: {
      cSEUser: CSEUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cSEUserRoute;
