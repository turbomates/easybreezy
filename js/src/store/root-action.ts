import { routerActions as router } from "connected-react-router";
import * as auth from "../features/auth/actions";
import * as humanResource from "../features/human-resouce/actions";
import * as account from "../features/account/actions";
import * as location from "../features/location/actions";

export default {
  router,
  auth,
  humanResource,
  account,
  location,
};
