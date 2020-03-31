import { Failed, Success } from "MyTypes";
import { Location } from "LocationModels";
import { locationsData } from "./data";

type UsersLocationsResponse = Promise<Success<Location[]> | Failed>;

export const fetchLocations = (): UsersLocationsResponse => {
  return Promise.resolve({
    success: true,
    data: locationsData,
  });
};
