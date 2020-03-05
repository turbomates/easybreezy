import React from "react";
import { Provider } from "react-redux";
import { ConnectedRouter } from "connected-react-router";

import store, { history } from "./store";
import { Routes } from "./Routes";
import { ErrorBoundary } from "./components/ErrorBoundary";

export const App: React.FC = () => (
  <ErrorBoundary>
    <Provider store={store}>
      <ConnectedRouter history={history}>
        <Routes />
      </ConnectedRouter>
    </Provider>
  </ErrorBoundary>
);
