import { LocationResponse, Location } from "LocationModels";
import { Failed, Success } from "MyTypes";
import { api } from "./api";

export const fetchAll = () =>
  api
    .get<LocationResponse>("/hr/locations")
    .then<Success<Location[]>>((resp) => ({
      success: true,
      data: resp.data.data.locations,
    }))
    .catch<Failed>(() => ({ success: false, reason: "Error" }));

export const create = (name: string) =>
  api
    .post("/hr/locations", { name })
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<Failed>(() => ({ success: false, reason: "Error" }));

export const remove = (locationId: string) =>
  api
    .delete(`hr/locations/${locationId}`)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<Failed>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));
