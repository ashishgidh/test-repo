import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClusterCountryDetailComponent } from './cluster-country-detail.component';

describe('ClusterCountry Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClusterCountryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClusterCountryDetailComponent,
              resolve: { clusterCountry: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ClusterCountryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load clusterCountry on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClusterCountryDetailComponent);

      // THEN
      expect(instance.clusterCountry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
