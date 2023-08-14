import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PortfolioSegmentComponent } from './list/portfolio-segment.component';
import { PortfolioSegmentDetailComponent } from './detail/portfolio-segment-detail.component';
import { PortfolioSegmentUpdateComponent } from './update/portfolio-segment-update.component';
import PortfolioSegmentResolve from './route/portfolio-segment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const portfolioSegmentRoute: Routes = [
  {
    path: '',
    component: PortfolioSegmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortfolioSegmentDetailComponent,
    resolve: {
      portfolioSegment: PortfolioSegmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortfolioSegmentUpdateComponent,
    resolve: {
      portfolioSegment: PortfolioSegmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortfolioSegmentUpdateComponent,
    resolve: {
      portfolioSegment: PortfolioSegmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default portfolioSegmentRoute;
