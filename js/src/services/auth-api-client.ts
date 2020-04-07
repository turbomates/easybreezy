import { SignInData, User } from "AuthModels";
import { Failed, Success } from "MyTypes";
import { api } from "./api";

export const check = () =>
  api
    .get<User>("/users/me")
    .then<Success<User>>(resp => ({ success: true, data: resp.data }))
    .catch<Failed>(resp => ({ success: false, reason: "" }));

export const signIn = (credentials: SignInData) =>
  api
    .post<{ data: string }>("/login", {
      email: credentials.email,
      password: credentials.password,
    })
    .then<Success<string>>(resp => ({ success: true, data: resp.data.data }))
    .catch<Failed>(resp => ({
      success: false,
      reason: resp?.response?.data?.error || "Error",
    }));

export const signOut = () => api.get("/logout").then(resp => resp.data);
