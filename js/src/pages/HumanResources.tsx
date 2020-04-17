import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { HumanResourceCalendar } from "features/human-resouce/components/HumanResourceCalendar";
import { fetchEmployeesAsync } from "features/human-resouce/actions";
import { calendarGroups, employees } from "features/human-resouce/selectors";

export const HumanResourcesPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchEmployeesAsync.request());
  }, [dispatch]);

  const { loading } = useSelector(employees);
  // const items = useSelector(vacationItems);
  const groups = useSelector(calendarGroups);

  if (loading) return null;

  return <HumanResourceCalendar groups={groups} />;
};
