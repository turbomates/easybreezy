import { routerActions } from "connected-react-router";
import * as authActions from "../features/auth/actions";
import * as humanResourceActions from "../features/human-resouce/actions";
import * as accountActions from "../features/account/actions";

export default {
  router: routerActions,
  auth: authActions,
  humanResource: humanResourceActions,
  account: accountActions,
};
