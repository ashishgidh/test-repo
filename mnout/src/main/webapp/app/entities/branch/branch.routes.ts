import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BranchComponent } from './list/branch.component';
import { BranchDetailComponent } from './detail/branch-detail.component';
import { BranchUpdateComponent } from './update/branch-update.component';
import BranchResolve from './route/branch-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const branchRoute: Routes = [
  {
    path: '',
    component: BranchComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BranchDetailComponent,
    resolve: {
      branch: BranchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BranchUpdateComponent,
    resolve: {
      branch: BranchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BranchUpdateComponent,
    resolve: {
      branch: BranchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default branchRoute;
