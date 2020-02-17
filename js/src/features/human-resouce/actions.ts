import { UsersListing, UsersParams, UserDetails } from "HumanResourceModels";
import { createAsyncAction } from "typesafe-actions";

export const fetchUsersAsync = createAsyncAction(
  "FETCH_USERS_REQUEST",
  "FETCH_USERS_SUCCESS",
  "FETCH_USERS_FAILURE",
)<UsersParams, UsersListing, string>();

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
