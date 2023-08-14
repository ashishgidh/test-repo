import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILOB } from '../lob.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../lob.test-samples';

import { LOBService } from './lob.service';

const requireRestSample: ILOB = {
  ...sampleWithRequiredData,
};

describe('LOB Service', () => {
  let service: LOBService;
  let httpMock: HttpTestingController;
  let expectedResult: ILOB | ILOB[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LOBService);
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

    it('should create a LOB', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const lOB = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lOB).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LOB', () => {
      const lOB = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lOB).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LOB', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LOB', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LOB', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLOBToCollectionIfMissing', () => {
      it('should add a LOB to an empty array', () => {
        const lOB: ILOB = sampleWithRequiredData;
        expectedResult = service.addLOBToCollectionIfMissing([], lOB);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lOB);
      });

      it('should not add a LOB to an array that contains it', () => {
        const lOB: ILOB = sampleWithRequiredData;
        const lOBCollection: ILOB[] = [
          {
            ...lOB,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLOBToCollectionIfMissing(lOBCollection, lOB);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LOB to an array that doesn't contain it", () => {
        const lOB: ILOB = sampleWithRequiredData;
        const lOBCollection: ILOB[] = [sampleWithPartialData];
        expectedResult = service.addLOBToCollectionIfMissing(lOBCollection, lOB);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lOB);
      });

      it('should add only unique LOB to an array', () => {
        const lOBArray: ILOB[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const lOBCollection: ILOB[] = [sampleWithRequiredData];
        expectedResult = service.addLOBToCollectionIfMissing(lOBCollection, ...lOBArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lOB: ILOB = sampleWithRequiredData;
        const lOB2: ILOB = sampleWithPartialData;
        expectedResult = service.addLOBToCollectionIfMissing([], lOB, lOB2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lOB);
        expect(expectedResult).toContain(lOB2);
      });

      it('should accept null and undefined values', () => {
        const lOB: ILOB = sampleWithRequiredData;
        expectedResult = service.addLOBToCollectionIfMissing([], null, lOB, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lOB);
      });

      it('should return initial array if no LOB is added', () => {
        const lOBCollection: ILOB[] = [sampleWithRequiredData];
        expectedResult = service.addLOBToCollectionIfMissing(lOBCollection, undefined, null);
        expect(expectedResult).toEqual(lOBCollection);
      });
    });

    describe('compareLOB', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLOB(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLOB(entity1, entity2);
        const compareResult2 = service.compareLOB(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLOB(entity1, entity2);
        const compareResult2 = service.compareLOB(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLOB(entity1, entity2);
        const compareResult2 = service.compareLOB(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
