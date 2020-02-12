import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { users } from "../features/human-resouce/selectors";
import { fetchUsersAsync } from "../features/human-resouce/actions";
import { HumanResourceList } from "../features/human-resouce/components/HumanResourceList";
import { UsersParams } from "HumanResourceModels";

export const UsersPage = () => {
  const dispatch = useDispatch();
  const load = (params: UsersParams) =>
    dispatch(fetchUsersAsync.request(params));

  useEffect(() => {
    load({});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const usersData = useSelector(users);

  return <HumanResourceList users={usersData} load={load} />;
};
