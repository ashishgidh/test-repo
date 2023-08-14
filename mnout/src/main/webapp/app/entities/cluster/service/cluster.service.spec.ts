import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICluster } from '../cluster.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cluster.test-samples';

import { ClusterService } from './cluster.service';

const requireRestSample: ICluster = {
  ...sampleWithRequiredData,
};

describe('Cluster Service', () => {
  let service: ClusterService;
  let httpMock: HttpTestingController;
  let expectedResult: ICluster | ICluster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClusterService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Cluster', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cluster = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cluster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cluster', () => {
      const cluster = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cluster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cluster', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cluster', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cluster', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClusterToCollectionIfMissing', () => {
      it('should add a Cluster to an empty array', () => {
        const cluster: ICluster = sampleWithRequiredData;
        expectedResult = service.addClusterToCollectionIfMissing([], cluster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cluster);
      });

      it('should not add a Cluster to an array that contains it', () => {
        const cluster: ICluster = sampleWithRequiredData;
        const clusterCollection: ICluster[] = [
          {
            ...cluster,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, cluster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cluster to an array that doesn't contain it", () => {
        const cluster: ICluster = sampleWithRequiredData;
        const clusterCollection: ICluster[] = [sampleWithPartialData];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, cluster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cluster);
      });

      it('should add only unique Cluster to an array', () => {
        const clusterArray: ICluster[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const clusterCollection: ICluster[] = [sampleWithRequiredData];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, ...clusterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cluster: ICluster = sampleWithRequiredData;
        const cluster2: ICluster = sampleWithPartialData;
        expectedResult = service.addClusterToCollectionIfMissing([], cluster, cluster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cluster);
        expect(expectedResult).toContain(cluster2);
      });

      it('should accept null and undefined values', () => {
        const cluster: ICluster = sampleWithRequiredData;
        expectedResult = service.addClusterToCollectionIfMissing([], null, cluster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cluster);
      });

      it('should return initial array if no Cluster is added', () => {
        const clusterCollection: ICluster[] = [sampleWithRequiredData];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, undefined, null);
        expect(expectedResult).toEqual(clusterCollection);
      });
    });

    describe('compareCluster', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCluster(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCluster(entity1, entity2);
        const compareResult2 = service.compareCluster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCluster(entity1, entity2);
        const compareResult2 = service.compareCluster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCluster(entity1, entity2);
        const compareResult2 = service.compareCluster(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
