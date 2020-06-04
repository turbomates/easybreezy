import React from "react";
import { useSelector } from "react-redux";
import { Route, Switch } from "react-router";

import { Main } from "layouts/Main";
import { Project } from "./layouts/Project";
import { HumanResourcesPage } from "./pages/HumanResources";
import { UserDetailsPage } from "./pages/UserDetails";
import { LocationsPage } from "./pages/Locations";
import { NotFoundPage } from "./pages/NotFound";
import { LoginPage } from "./pages/Login";
import { ProjectsPage } from "./pages/ProjectsPage";
import { ProjectPage } from "./pages/ProjectPage";
import { ProjectRolePage } from "./pages/ProjectRolePage";
import { ProjectTeamsPage } from "./pages/ProjectTeamsPage";
import { ProjectTeamPage } from "./pages/ProjectTeamPage";

import { isAuthorized } from "./features/auth/selectors";
import { canRender } from "./features/app/selectors";
import { Preloader } from "./features/app/components/Preloader";

export const Routes: React.FC = () => {
  const isUserAuthorized = useSelector(isAuthorized);
  const canStartRender = useSelector(canRender);

  if (!canStartRender) return <Preloader />;

  if (!isUserAuthorized) return <LoginPage />;

  return (
    <Main>
      <Switch>
        <Route exact path="/" component={HumanResourcesPage} />
        <Route path="/human-resources" component={HumanResourcesPage} />
        <Route path="/users/:id" component={UserDetailsPage} />
        <Route path="/locations" component={LocationsPage} />
        <Route path="/projects" component={ProjectsPage} exact />
        <Route path="/projects/:slug">
          <Project>
            <Switch>
              <Route
                path="/projects/:slug/teams/:id"
                component={ProjectTeamPage}
              />
              <Route
                path="/projects/:slug/teams"
                component={ProjectTeamsPage}
              />
              <Route path="/projects/:slug/role" component={ProjectRolePage} />
              <Route path="/projects/:slug" component={ProjectPage} />
            </Switch>
          </Project>
        </Route>
        <Route component={NotFoundPage} />
      </Switch>
    </Main>
  );
};
