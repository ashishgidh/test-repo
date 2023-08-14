import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PortfolioSegmentDetailComponent } from './portfolio-segment-detail.component';

describe('PortfolioSegment Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioSegmentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PortfolioSegmentDetailComponent,
              resolve: { portfolioSegment: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PortfolioSegmentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load portfolioSegment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PortfolioSegmentDetailComponent);

      // THEN
      expect(instance.portfolioSegment).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
