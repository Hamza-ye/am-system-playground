import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { IWhMovement, WhMovement } from '../wh-movement.model';

import { WhMovementService } from './wh-movement.service';

describe('Service Tests', () => {
  describe('WhMovement Service', () => {
    let service: WhMovementService;
    let httpMock: HttpTestingController;
    let elemDefault: IWhMovement;
    let expectedResult: IWhMovement | IWhMovement[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WhMovementService);
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

      it('should create a WhMovement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WhMovement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WhMovement', () => {
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

      it('should partial update a WhMovement', () => {
        const patchObject = Object.assign(
          {
            quantity: 1,
            confirmedByOtherSide: true,
          },
          new WhMovement()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WhMovement', () => {
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

      it('should delete a WhMovement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWhMovementToCollectionIfMissing', () => {
        it('should add a WhMovement to an empty array', () => {
          const whMovement: IWhMovement = { id: 123 };
          expectedResult = service.addWhMovementToCollectionIfMissing([], whMovement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(whMovement);
        });

        it('should not add a WhMovement to an array that contains it', () => {
          const whMovement: IWhMovement = { id: 123 };
          const whMovementCollection: IWhMovement[] = [
            {
              ...whMovement,
            },
            { id: 456 },
          ];
          expectedResult = service.addWhMovementToCollectionIfMissing(whMovementCollection, whMovement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WhMovement to an array that doesn't contain it", () => {
          const whMovement: IWhMovement = { id: 123 };
          const whMovementCollection: IWhMovement[] = [{ id: 456 }];
          expectedResult = service.addWhMovementToCollectionIfMissing(whMovementCollection, whMovement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(whMovement);
        });

        it('should add only unique WhMovement to an array', () => {
          const whMovementArray: IWhMovement[] = [{ id: 123 }, { id: 456 }, { id: 50530 }];
          const whMovementCollection: IWhMovement[] = [{ id: 123 }];
          expectedResult = service.addWhMovementToCollectionIfMissing(whMovementCollection, ...whMovementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const whMovement: IWhMovement = { id: 123 };
          const whMovement2: IWhMovement = { id: 456 };
          expectedResult = service.addWhMovementToCollectionIfMissing([], whMovement, whMovement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(whMovement);
          expect(expectedResult).toContain(whMovement2);
        });

        it('should accept null and undefined values', () => {
          const whMovement: IWhMovement = { id: 123 };
          expectedResult = service.addWhMovementToCollectionIfMissing([], null, whMovement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(whMovement);
        });

        it('should return initial array if no WhMovement is added', () => {
          const whMovementCollection: IWhMovement[] = [{ id: 123 }];
          expectedResult = service.addWhMovementToCollectionIfMissing(whMovementCollection, undefined, null);
          expect(expectedResult).toEqual(whMovementCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
