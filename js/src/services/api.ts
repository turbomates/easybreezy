import axios from "axios";

const BASE_URL = "https://easybreezy.tmsoft.dev/api";

export const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
});
