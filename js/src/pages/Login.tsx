import React from "react";

import { LoginForm } from "../features/auth/components/LoginForm";

import "./Login.scss";

export const LoginPage: React.FC = () => (
  <main className="login-page">
    <LoginForm />
  </main>
);
