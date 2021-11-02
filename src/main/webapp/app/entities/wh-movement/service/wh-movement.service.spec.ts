import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { IWHMovement, WHMovement } from '../wh-movement.model';

import { WHMovementService } from './wh-movement.service';

describe('Service Tests', () => {
  describe('WHMovement Service', () => {
    let service: WHMovementService;
    let httpMock: HttpTestingController;
    let elemDefault: IWHMovement;
    let expectedResult: IWHMovement | IWHMovement[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WHMovementService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        movementType: MovementType.IN,
        quantity: 0,
        reconciliationSource: 'AAAAAAA',
        reconciliationDestination: 'AAAAAAA',
        confirmedByOtherSide: false,
        comment: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WHMovement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WHMovement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WHMovement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            movementType: 'BBBBBB',
            quantity: 1,
            reconciliationSource: 'BBBBBB',
            reconciliationDestination: 'BBBBBB',
            confirmedByOtherSide: true,
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WHMovement', () => {
        const patchObject = Object.assign(
          {
            movementType: 'BBBBBB',
            quantity: 1,
            reconciliationSource: 'BBBBBB',
            reconciliationDestination: 'BBBBBB',
          },
          new WHMovement()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WHMovement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            movementType: 'BBBBBB',
            quantity: 1,
            reconciliationSource: 'BBBBBB',
            reconciliationDestination: 'BBBBBB',
            confirmedByOtherSide: true,
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a WHMovement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWHMovementToCollectionIfMissing', () => {
        it('should add a WHMovement to an empty array', () => {
          const wHMovement: IWHMovement = { id: 123 };
          expectedResult = service.addWHMovementToCollectionIfMissing([], wHMovement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wHMovement);
        });

        it('should not add a WHMovement to an array that contains it', () => {
          const wHMovement: IWHMovement = { id: 123 };
          const wHMovementCollection: IWHMovement[] = [
            {
              ...wHMovement,
            },
            { id: 456 },
          ];
          expectedResult = service.addWHMovementToCollectionIfMissing(wHMovementCollection, wHMovement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WHMovement to an array that doesn't contain it", () => {
          const wHMovement: IWHMovement = { id: 123 };
          const wHMovementCollection: IWHMovement[] = [{ id: 456 }];
          expectedResult = service.addWHMovementToCollectionIfMissing(wHMovementCollection, wHMovement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wHMovement);
        });

        it('should add only unique WHMovement to an array', () => {
          const wHMovementArray: IWHMovement[] = [{ id: 123 }, { id: 456 }, { id: 88047 }];
          const wHMovementCollection: IWHMovement[] = [{ id: 123 }];
          expectedResult = service.addWHMovementToCollectionIfMissing(wHMovementCollection, ...wHMovementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const wHMovement: IWHMovement = { id: 123 };
          const wHMovement2: IWHMovement = { id: 456 };
          expectedResult = service.addWHMovementToCollectionIfMissing([], wHMovement, wHMovement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wHMovement);
          expect(expectedResult).toContain(wHMovement2);
        });

        it('should accept null and undefined values', () => {
          const wHMovement: IWHMovement = { id: 123 };
          expectedResult = service.addWHMovementToCollectionIfMissing([], null, wHMovement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wHMovement);
        });

        it('should return initial array if no WHMovement is added', () => {
          const wHMovementCollection: IWHMovement[] = [{ id: 123 }];
          expectedResult = service.addWHMovementToCollectionIfMissing(wHMovementCollection, undefined, null);
          expect(expectedResult).toEqual(wHMovementCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
