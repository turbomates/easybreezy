import { UserVacations, UserDetails } from "HumanResourceModels";
import { createAsyncAction } from "typesafe-actions";

export const fetchUsersVacationsAsync = createAsyncAction(
  "FETCH_USERS_VACATIONS_REQUEST",
  "FETCH_USERS_VACATIONS_SUCCESS",
  "FETCH_USERS_VACATIONS_FAILURE",
)<undefined, UserVacations[], string>();

export const fetchUserDetailsAsync = createAsyncAction(
  "FETCH_USER_DETAILS_REQUEST",
  "FETCH_USER_DETAILS_SUCCESS",
  "FETCH_USER_DETAILS_FAILURE",
)<string, UserDetails, string>();

export const fetchProfileAsync = createAsyncAction(
  "FETCH_PROFILE_REQUEST",
  "FETCH_PROFILE_SUCCESS",
  "FETCH_PROFILE_FAILURE",
)<undefined, UserDetails, string>();
