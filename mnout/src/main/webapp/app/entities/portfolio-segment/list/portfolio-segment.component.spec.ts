import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PortfolioSegmentService } from '../service/portfolio-segment.service';

import { PortfolioSegmentComponent } from './portfolio-segment.component';

describe('PortfolioSegment Management Component', () => {
  let comp: PortfolioSegmentComponent;
  let fixture: ComponentFixture<PortfolioSegmentComponent>;
  let service: PortfolioSegmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'portfolio-segment', component: PortfolioSegmentComponent }]),
        HttpClientTestingModule,
        PortfolioSegmentComponent,
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
      .overrideTemplate(PortfolioSegmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PortfolioSegmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PortfolioSegmentService);

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
    expect(comp.portfolioSegments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to portfolioSegmentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getPortfolioSegmentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPortfolioSegmentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
