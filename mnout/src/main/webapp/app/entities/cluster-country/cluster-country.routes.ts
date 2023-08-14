import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClusterCountryComponent } from './list/cluster-country.component';
import { ClusterCountryDetailComponent } from './detail/cluster-country-detail.component';
import { ClusterCountryUpdateComponent } from './update/cluster-country-update.component';
import ClusterCountryResolve from './route/cluster-country-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const clusterCountryRoute: Routes = [
  {
    path: '',
    component: ClusterCountryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClusterCountryDetailComponent,
    resolve: {
      clusterCountry: ClusterCountryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClusterCountryUpdateComponent,
    resolve: {
      clusterCountry: ClusterCountryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClusterCountryUpdateComponent,
    resolve: {
      clusterCountry: ClusterCountryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clusterCountryRoute;
