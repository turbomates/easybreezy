import { SignInData } from "AuthModels";
import React from "react";
import useForm from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";

import { signInAsync } from "../actions";
import { signInFailedReason } from "../selectors";

export const LoginForm: React.FC = () => {
  const { errors, handleSubmit, register } = useForm<SignInData>({});
  const dispatch = useDispatch();
  const error = useSelector(signInFailedReason);

  function onSubmit(data: SignInData) {
    dispatch(signInAsync.request(data));
  }

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <input
            name="username"
            type="text"
            placeholder="Username"
            ref={register({ required: true })}
          />
          <span>{errors.username && "Required"}</span>
        </div>
        <div>
          <input
            name="password"
            type="password"
            placeholder="Password"
            ref={register({ required: true })}
          />
          <span>{errors.password && "Required"}</span>
        </div>
        <div>
          <span>{error}</span>
          <input type="submit" value="Login" />
        </div>
      </form>
    </div>
  );
};
