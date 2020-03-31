declare module "LocationModels" {
  export type Location = {
    id: string;
    name: string;
  };

  export type LocationResponse = {
    data: {
      locations: Location[];
    };
  };
}
