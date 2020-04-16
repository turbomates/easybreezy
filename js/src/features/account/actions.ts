import { createAsyncAction } from "typesafe-actions";
import { Profile } from "AccountModules";

export const fetchProfileAsync = createAsyncAction(
  "FETCH_PROFILE_REQUEST",
  "FETCH_PROFILE_SUCCESS",
  "FETCH_PROFILE_FAILURE",
)<undefined, Profile, string>();
