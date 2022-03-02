export interface IPlane {
  id?: number;
  plate?: string;
  type?: string | null;
  age?: number | null;
  serialNumber?: string | null;
}

export class Plane implements IPlane {
  constructor(
    public id?: number,
    public plate?: string,
    public type?: string | null,
    public age?: number | null,
    public serialNumber?: string | null
  ) {}
}

export function getPlaneIdentifier(plane: IPlane): number | undefined {
  return plane.id;
}
