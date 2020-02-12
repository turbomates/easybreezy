import { UserDetails } from "HumanResourceModels";
import { createAsyncAction } from "typesafe-actions";

export const fetchProfileAsync = createAsyncAction(
  "FETCH_PROFILE_REQUEST",
  "FETCH_PROFILE_SUCCESS",
  "FETCH_PROFILE_FAILURE",
)<undefined, UserDetails, string>();
