import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProgramDetailComponent } from './program-detail.component';

describe('Program Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProgramDetailComponent,
              resolve: { program: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ProgramDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load program on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProgramDetailComponent);

      // THEN
      expect(instance.program).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
