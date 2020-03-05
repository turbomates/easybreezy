import React from "react";
import { useSelector } from "react-redux";
import { Route, Switch } from "react-router";

import { Main } from "layouts/Main";
import { HomePage } from "./pages/Home";
import { HumanResourcesPage } from "./pages/HumanResources";
import { UserDetailsPage } from "./pages/UserDetails";
import { NotFoundPage } from "./pages/NotFound";
// import { LoginPage } from "./pages/Login";

// import { isAuthorized } from "./features/auth/selectors";
import { canRender } from "./features/app/selectors";
import { Preloader } from "./features/app/components/Preloader";

export const Routes: React.FC = () => {
  // const isUserAuthorized = useSelector(isAuthorized);
  const canStartRender = useSelector(canRender);

  if (!canStartRender) return <Preloader />;

  // if (!isUserAuthorized) return <LoginPage />;

  return (
    <Main>
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route path="/human-resources" component={HumanResourcesPage} />
        <Route path="/users/:id" component={UserDetailsPage} />
        <Route component={NotFoundPage} />
      </Switch>
    </Main>
  );
};
