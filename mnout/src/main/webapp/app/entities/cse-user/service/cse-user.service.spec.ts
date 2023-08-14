import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICSEUser } from '../cse-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cse-user.test-samples';

import { CSEUserService } from './cse-user.service';

const requireRestSample: ICSEUser = {
  ...sampleWithRequiredData,
};

describe('CSEUser Service', () => {
  let service: CSEUserService;
  let httpMock: HttpTestingController;
  let expectedResult: ICSEUser | ICSEUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CSEUserService);
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

    it('should create a CSEUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cSEUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cSEUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CSEUser', () => {
      const cSEUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cSEUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CSEUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CSEUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CSEUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCSEUserToCollectionIfMissing', () => {
      it('should add a CSEUser to an empty array', () => {
        const cSEUser: ICSEUser = sampleWithRequiredData;
        expectedResult = service.addCSEUserToCollectionIfMissing([], cSEUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cSEUser);
      });

      it('should not add a CSEUser to an array that contains it', () => {
        const cSEUser: ICSEUser = sampleWithRequiredData;
        const cSEUserCollection: ICSEUser[] = [
          {
            ...cSEUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCSEUserToCollectionIfMissing(cSEUserCollection, cSEUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CSEUser to an array that doesn't contain it", () => {
        const cSEUser: ICSEUser = sampleWithRequiredData;
        const cSEUserCollection: ICSEUser[] = [sampleWithPartialData];
        expectedResult = service.addCSEUserToCollectionIfMissing(cSEUserCollection, cSEUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cSEUser);
      });

      it('should add only unique CSEUser to an array', () => {
        const cSEUserArray: ICSEUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cSEUserCollection: ICSEUser[] = [sampleWithRequiredData];
        expectedResult = service.addCSEUserToCollectionIfMissing(cSEUserCollection, ...cSEUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cSEUser: ICSEUser = sampleWithRequiredData;
        const cSEUser2: ICSEUser = sampleWithPartialData;
        expectedResult = service.addCSEUserToCollectionIfMissing([], cSEUser, cSEUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cSEUser);
        expect(expectedResult).toContain(cSEUser2);
      });

      it('should accept null and undefined values', () => {
        const cSEUser: ICSEUser = sampleWithRequiredData;
        expectedResult = service.addCSEUserToCollectionIfMissing([], null, cSEUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cSEUser);
      });

      it('should return initial array if no CSEUser is added', () => {
        const cSEUserCollection: ICSEUser[] = [sampleWithRequiredData];
        expectedResult = service.addCSEUserToCollectionIfMissing(cSEUserCollection, undefined, null);
        expect(expectedResult).toEqual(cSEUserCollection);
      });
    });

    describe('compareCSEUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCSEUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCSEUser(entity1, entity2);
        const compareResult2 = service.compareCSEUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCSEUser(entity1, entity2);
        const compareResult2 = service.compareCSEUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCSEUser(entity1, entity2);
        const compareResult2 = service.compareCSEUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
