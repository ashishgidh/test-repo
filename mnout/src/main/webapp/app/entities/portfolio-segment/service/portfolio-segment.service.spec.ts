import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPortfolioSegment } from '../portfolio-segment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../portfolio-segment.test-samples';

import { PortfolioSegmentService } from './portfolio-segment.service';

const requireRestSample: IPortfolioSegment = {
  ...sampleWithRequiredData,
};

describe('PortfolioSegment Service', () => {
  let service: PortfolioSegmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IPortfolioSegment | IPortfolioSegment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PortfolioSegmentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PortfolioSegment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const portfolioSegment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(portfolioSegment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PortfolioSegment', () => {
      const portfolioSegment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(portfolioSegment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PortfolioSegment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PortfolioSegment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PortfolioSegment', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPortfolioSegmentToCollectionIfMissing', () => {
      it('should add a PortfolioSegment to an empty array', () => {
        const portfolioSegment: IPortfolioSegment = sampleWithRequiredData;
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing([], portfolioSegment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(portfolioSegment);
      });

      it('should not add a PortfolioSegment to an array that contains it', () => {
        const portfolioSegment: IPortfolioSegment = sampleWithRequiredData;
        const portfolioSegmentCollection: IPortfolioSegment[] = [
          {
            ...portfolioSegment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing(portfolioSegmentCollection, portfolioSegment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PortfolioSegment to an array that doesn't contain it", () => {
        const portfolioSegment: IPortfolioSegment = sampleWithRequiredData;
        const portfolioSegmentCollection: IPortfolioSegment[] = [sampleWithPartialData];
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing(portfolioSegmentCollection, portfolioSegment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(portfolioSegment);
      });

      it('should add only unique PortfolioSegment to an array', () => {
        const portfolioSegmentArray: IPortfolioSegment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const portfolioSegmentCollection: IPortfolioSegment[] = [sampleWithRequiredData];
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing(portfolioSegmentCollection, ...portfolioSegmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const portfolioSegment: IPortfolioSegment = sampleWithRequiredData;
        const portfolioSegment2: IPortfolioSegment = sampleWithPartialData;
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing([], portfolioSegment, portfolioSegment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(portfolioSegment);
        expect(expectedResult).toContain(portfolioSegment2);
      });

      it('should accept null and undefined values', () => {
        const portfolioSegment: IPortfolioSegment = sampleWithRequiredData;
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing([], null, portfolioSegment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(portfolioSegment);
      });

      it('should return initial array if no PortfolioSegment is added', () => {
        const portfolioSegmentCollection: IPortfolioSegment[] = [sampleWithRequiredData];
        expectedResult = service.addPortfolioSegmentToCollectionIfMissing(portfolioSegmentCollection, undefined, null);
        expect(expectedResult).toEqual(portfolioSegmentCollection);
      });
    });

    describe('comparePortfolioSegment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePortfolioSegment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePortfolioSegment(entity1, entity2);
        const compareResult2 = service.comparePortfolioSegment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePortfolioSegment(entity1, entity2);
        const compareResult2 = service.comparePortfolioSegment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePortfolioSegment(entity1, entity2);
        const compareResult2 = service.comparePortfolioSegment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
