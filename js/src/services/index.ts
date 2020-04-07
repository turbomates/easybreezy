import * as logger from "./logger-service";
import * as jwt from "./jwt-service";
import * as localStorage from "./local-storage-service";
import * as auth from "./auth-api-client";
import * as humanResource from "./stub/human-resource-api";
import * as location from "./location-api";

export default {
  logger,
  jwt,
  localStorage,
  api: {
    auth,
    humanResource,
    location,
  },
};
