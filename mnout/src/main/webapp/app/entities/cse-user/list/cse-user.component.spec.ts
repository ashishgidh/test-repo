import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CSEUserService } from '../service/cse-user.service';

import { CSEUserComponent } from './cse-user.component';

describe('CSEUser Management Component', () => {
  let comp: CSEUserComponent;
  let fixture: ComponentFixture<CSEUserComponent>;
  let service: CSEUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'cse-user', component: CSEUserComponent }]),
        HttpClientTestingModule,
        CSEUserComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CSEUserComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CSEUserComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CSEUserService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.cSEUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to cSEUserService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCSEUserIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCSEUserIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
