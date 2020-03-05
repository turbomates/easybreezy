import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { HumanResourceCalendar } from "features/human-resouce/components/HumanResourceCalendar";
import { fetchUsersVacationsAsync } from "features/human-resouce/actions";
import {
  vacations,
  vacationGroups,
  vacationItems,
} from "features/human-resouce/selectors";

export const HumanResourcesPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchUsersVacationsAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const { loading } = useSelector(vacations);
  const items = useSelector(vacationItems);
  const groups = useSelector(vacationGroups);

  if (loading) return null;

  return <HumanResourceCalendar items={items} groups={groups} />;
};
