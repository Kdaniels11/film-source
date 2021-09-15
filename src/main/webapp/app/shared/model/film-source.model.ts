export interface IFilmSource {
  id?: number;
  lastname?: string | null;
  firstname?: string | null;
  position?: string | null;
  location?: string | null;
  rate?: number | null;
}

export const defaultValue: Readonly<IFilmSource> = {};
