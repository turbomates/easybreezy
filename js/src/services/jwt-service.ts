import jwtDecode from "jwt-decode";
import * as LocalStorage from "./local-storage-service";

const localStorageKey = "jwt";

export const set = (jwt: string) => LocalStorage.set(localStorageKey, jwt);
export const get = () => LocalStorage.get<string>(localStorageKey);
export const remove = () => LocalStorage.remove(localStorageKey);
export const decode = <T>(jwt: string) => jwtDecode<T>(jwt);
