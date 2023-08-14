import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClusterService } from '../service/cluster.service';

import { ClusterComponent } from './cluster.component';

describe('Cluster Management Component', () => {
  let comp: ClusterComponent;
  let fixture: ComponentFixture<ClusterComponent>;
  let service: ClusterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'cluster', component: ClusterComponent }]),
        HttpClientTestingModule,
        ClusterComponent,
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
      .overrideTemplate(ClusterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClusterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClusterService);

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
    expect(comp.clusters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to clusterService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getClusterIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getClusterIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
