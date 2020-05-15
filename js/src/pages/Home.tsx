import React from "react";
import { withActivity } from "../features/auth/components/withActivity";

export const HomePage: React.FC = withActivity("USERS_MANAGE")(() => (
  <div className="content">Home page</div>
));
