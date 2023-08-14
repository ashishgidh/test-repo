import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClusterCountryService } from '../service/cluster-country.service';

import { ClusterCountryComponent } from './cluster-country.component';

describe('ClusterCountry Management Component', () => {
  let comp: ClusterCountryComponent;
  let fixture: ComponentFixture<ClusterCountryComponent>;
  let service: ClusterCountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'cluster-country', component: ClusterCountryComponent }]),
        HttpClientTestingModule,
        ClusterCountryComponent,
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
      .overrideTemplate(ClusterCountryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClusterCountryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClusterCountryService);

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
    expect(comp.clusterCountries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to clusterCountryService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getClusterCountryIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getClusterCountryIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
