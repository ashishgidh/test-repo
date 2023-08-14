import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClusterDetailComponent } from './cluster-detail.component';

describe('Cluster Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClusterDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClusterDetailComponent,
              resolve: { cluster: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ClusterDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cluster on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClusterDetailComponent);

      // THEN
      expect(instance.cluster).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
