import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'branch',
        data: { pageTitle: 'Branches' },
        loadChildren: () => import('./branch/branch.routes'),
      },
      {
        path: 'client',
        data: { pageTitle: 'Clients' },
        loadChildren: () => import('./client/client.routes'),
      },
      {
        path: 'cluster',
        data: { pageTitle: 'Clusters' },
        loadChildren: () => import('./cluster/cluster.routes'),
      },
      {
        path: 'cluster-country',
        data: { pageTitle: 'ClusterCountries' },
        loadChildren: () => import('./cluster-country/cluster-country.routes'),
      },
      {
        path: 'cse-user',
        data: { pageTitle: 'CSEUsers' },
        loadChildren: () => import('./cse-user/cse-user.routes'),
      },
      {
        path: 'lob',
        data: { pageTitle: 'LOBS' },
        loadChildren: () => import('./lob/lob.routes'),
      },
      {
        path: 'portfolio-segment',
        data: { pageTitle: 'PortfolioSegments' },
        loadChildren: () => import('./portfolio-segment/portfolio-segment.routes'),
      },
      {
        path: 'program',
        data: { pageTitle: 'Programs' },
        loadChildren: () => import('./program/program.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
