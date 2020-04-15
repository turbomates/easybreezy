import * as logger from "./logger-service";
import * as jwt from "./jwt-service";
import * as localStorage from "./local-storage-service";
import * as auth from "./auth-api-client";
import * as location from "./location-api";
import * as humanResource from "./human-resource-api";

export default {
  logger,
  jwt,
  localStorage,
  api: {
    auth,
    location,
    humanResource,
  },
};
