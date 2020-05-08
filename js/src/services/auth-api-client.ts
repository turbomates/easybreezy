import { SignInData, User, RulesMap } from "AuthModels";
import { Failure, Success } from "MyTypes";
import { api } from "./api";

export const check = () =>
  api
    .get<User>("/users/me")
    .then<Success<User>>((resp) => ({ success: true, data: resp.data }))
    .catch<Failure>((resp) => ({ success: false, reason: "" }));

export const signIn = (credentials: SignInData) =>
  api
    .post<{ data: string }>("/login", {
      email: credentials.email,
      password: credentials.password,
    })
    .then<Success<string>>((resp) => ({ success: true, data: resp.data.data }))
    .catch<Failure>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));

export const signOut = () => api.get("/logout").then((resp) => resp.data);

export const fetchRoles = () =>
  api
    .get("authorization/rules")
    .then<Success<RulesMap>>((resp) => ({
      success: true,
      data: resp.data.data,
    }))
    .catch<Failure>((resp) => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));
