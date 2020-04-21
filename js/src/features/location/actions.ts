import { FormError } from "MyTypes";
import { Location, LocationForm } from "LocationModels";
import { createAsyncAction } from "typesafe-actions";

export const fetchLocationsAsync = createAsyncAction(
  "FETCH_LOCATIONS_REQUEST",
  "FETCH_LOCATIONS_SUCCESS",
  "FETCH_LOCATIONS_FAILURE",
)<undefined, Location[], string>();

export const createLocationAsync = createAsyncAction(
  "CREATE_LOCATION_REQUEST",
  "CREATE_LOCATION_SUCCESS",
  "CREATE_LOCATION_FAILURE",
)<LocationForm, undefined, FormError[]>();

export const removeLocationAsync = createAsyncAction(
  "REMOVE_LOCATION_REQUEST",
  "REMOVE_LOCATION_SUCCESS",
  "REMOVE_LOCATION_FAILURE",
)<string, undefined, string>();
