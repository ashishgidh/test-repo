import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CSEUserDetailComponent } from './cse-user-detail.component';

describe('CSEUser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CSEUserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CSEUserDetailComponent,
              resolve: { cSEUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CSEUserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cSEUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CSEUserDetailComponent);

      // THEN
      expect(instance.cSEUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
