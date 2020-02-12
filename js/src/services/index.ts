import * as logger from "./logger-service";
import * as auth from "./auth-api-client";
import * as localStorage from "./local-storage-service";
import * as humanResource from "./stub/human-resource-api";

export default {
  logger,
  localStorage,
  api: {
    auth,
    humanResource,
  },
};
