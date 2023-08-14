import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LOBComponent } from './list/lob.component';
import { LOBDetailComponent } from './detail/lob-detail.component';
import { LOBUpdateComponent } from './update/lob-update.component';
import LOBResolve from './route/lob-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const lOBRoute: Routes = [
  {
    path: '',
    component: LOBComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LOBDetailComponent,
    resolve: {
      lOB: LOBResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LOBUpdateComponent,
    resolve: {
      lOB: LOBResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LOBUpdateComponent,
    resolve: {
      lOB: LOBResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default lOBRoute;
