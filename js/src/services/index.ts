import * as logger from "./logger-service";
import * as auth from "./auth-api-client";
import * as localStorage from "./local-storage-service";
import * as humanResource from "./stub/human-resource-api";
import * as location from "./location-api";

export default {
  logger,
  localStorage,
  api: {
    auth,
    humanResource,
    location,
  },
};
