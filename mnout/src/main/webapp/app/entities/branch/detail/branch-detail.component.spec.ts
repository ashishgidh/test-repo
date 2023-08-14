import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BranchDetailComponent } from './branch-detail.component';

describe('Branch Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BranchDetailComponent,
              resolve: { branch: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(BranchDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load branch on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BranchDetailComponent);

      // THEN
      expect(instance.branch).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
