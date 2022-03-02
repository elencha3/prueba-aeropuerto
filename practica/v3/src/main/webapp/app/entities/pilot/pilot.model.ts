export interface IPilot {
  id?: number;
  name?: string;
  dni?: string;
  adress?: string;
  email?: string;
  horasVuelo?: number;
  pictureContentType?: string | null;
  picture?: string | null;
}

export class Pilot implements IPilot {
  constructor(
    public id?: number,
    public name?: string,
    public dni?: string,
    public adress?: string,
    public email?: string,
    public horasVuelo?: number,
    public pictureContentType?: string | null,
    public picture?: string | null
  ) {}
}

export function getPilotIdentifier(pilot: IPilot): number | undefined {
  return pilot.id;
}
