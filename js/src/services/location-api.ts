import {
  LocationResponse,
  Location,
  LocationForm,
  AssignLocationForm,
  EmployeeLocationsResponse,
  EmployeeLocationsMap,
  EditEmployeeLocationData,
} from "LocationModels";
import { Failure, Success, FormFailure } from "MyTypes";
import { api } from "./api";

export const fetchAll = () =>
  api
    .get<LocationResponse>("/hr/locations")
    .then<Success<Location[]>>((resp) => ({
      success: true,
      data: resp.data.data.locations,
    }))
    .catch<Failure>((resp) => ({ success: false, reason: "Error" }));

export const create = (form: LocationForm) =>
  api
    .post("/hr/locations", form)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const remove = (locationId: string) =>
  api
    .delete(`hr/locations/${locationId}`)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<Failure>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));

export const fetchUserLocations = () =>
  api
    .get<EmployeeLocationsResponse>("/hr/locations/user")
    .then<Success<EmployeeLocationsMap>>(({ data }) => ({
      success: true,
      data: data.data.userLocations,
    }))
    .catch<Failure>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));

export const assignUserLocations = (form: AssignLocationForm) =>
  api
    .post("/hr/locations/user", form)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const removeUserLocation = (userLocationId: string) =>
  api
    .delete(`/hr/locations/user/${userLocationId}`)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<Failure>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));

export const editUserLocation = (data: EditEmployeeLocationData) =>
  api
    .post(`/hr/locations/user/${data.employeeLocationId}`, data.form)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

// export const getUserLocation = (userLocationId: string) =>
//   api.get(`/hr/locations/user/${userLocationId}`);
