import { SignInData, User, RulesMap } from "AuthModels";
import { createAsyncAction } from "typesafe-actions";

export const signInAsync = createAsyncAction(
  "SIGN_IN_REQUEST",
  "SIGN_IN_SUCCESS",
  "SIGN_IN_FAILURE",
)<SignInData, User, string>();

export const signOutAsync = createAsyncAction(
  "SIGN_OUT_REQUEST",
  "SIGN_OUT_SUCCESS",
  "SIGN_OUT_FAILURE",
)<undefined, undefined, string>();

export const checkAuth = createAsyncAction(
  "CHECK_AUTH_REQUEST",
  "CHECK_AUTH_SUCCESS",
  "CHECK_AUTH_FAILURE",
)<undefined, User, undefined>();

export const fetchRulesAsync = createAsyncAction(
  "FETCH_AUTH_RULES_REQUEST",
  "FETCH_AUTH_RULES_SUCCESS",
  "FETCH_AUTH_RULES_FAILURE",
)<undefined, RulesMap, string>();
