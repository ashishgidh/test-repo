import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClusterComponent } from './list/cluster.component';
import { ClusterDetailComponent } from './detail/cluster-detail.component';
import { ClusterUpdateComponent } from './update/cluster-update.component';
import ClusterResolve from './route/cluster-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const clusterRoute: Routes = [
  {
    path: '',
    component: ClusterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClusterDetailComponent,
    resolve: {
      cluster: ClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClusterUpdateComponent,
    resolve: {
      cluster: ClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClusterUpdateComponent,
    resolve: {
      cluster: ClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clusterRoute;
