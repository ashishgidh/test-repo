import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProgramService } from '../service/program.service';

import { ProgramComponent } from './program.component';

describe('Program Management Component', () => {
  let comp: ProgramComponent;
  let fixture: ComponentFixture<ProgramComponent>;
  let service: ProgramService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'program', component: ProgramComponent }]),
        HttpClientTestingModule,
        ProgramComponent,
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
      .overrideTemplate(ProgramComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgramComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProgramService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
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
    expect(comp.programs?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to programService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getProgramIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProgramIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
