import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { HumanResourceCalendar } from "../features/human-resouce/components/HumanResourceCalendar";
import { fetchUsersVacationsAsync } from "features/human-resouce/actions";
import { vacations } from "features/human-resouce/selectors";

export const HumanResourcesPage = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchUsersVacationsAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const vacationsData = useSelector(vacations);

  if (vacationsData.loading) return null;

  return <HumanResourceCalendar items={vacationsData.items} />;
};
