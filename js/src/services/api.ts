import axios from "axios";

import { DOMAIN } from "../constants";
import * as jwt from "./jwt-service";

export const api = axios.create({
  baseURL: `${DOMAIN}/api`,
});

api.interceptors.request.use((config) => {
  const token = jwt.get();
  if (token && config.method !== "options") {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});
