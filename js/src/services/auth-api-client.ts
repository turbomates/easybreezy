import { SignInData, User } from "AuthModels";
import { Failed, Success } from "MyTypes";
import axios from "axios";

const BASE_URL = "http://24betting.dev.bettings.ch/api/v1";

export const check = () =>
  axios
    .get<User>(`${BASE_URL}/user`)
    .then<Success<User>>(resp => ({ success: true, data: resp.data }))
    .catch<Failed>(resp => ({ success: false, reason: "" }));

export const signIn = (credentials: SignInData) =>
  axios
    .post<User>(`${BASE_URL}/login_check`, {
      _username: credentials.username,
      _password: credentials.password,
    })
    .then<Success<User>>(resp => ({ success: true, data: resp.data }))
    .catch<Failed>(resp => ({
      success: false,
      reason: resp.response.data.error,
    }));

export const signOut = (username: string, password: string) =>
  axios.get(`${BASE_URL}/logout`).then(resp => resp.data);
