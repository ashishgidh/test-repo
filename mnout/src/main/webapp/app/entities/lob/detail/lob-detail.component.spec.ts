import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LOBDetailComponent } from './lob-detail.component';

describe('LOB Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LOBDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LOBDetailComponent,
              resolve: { lOB: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(LOBDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load lOB on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LOBDetailComponent);

      // THEN
      expect(instance.lOB).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
