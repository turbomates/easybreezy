import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { HumanResourceCalendar } from "features/human-resouce/components/HumanResourceCalendar/HumanResourceCalendar";
import {
  fetchEmployeesAsync,
  fetchAbsencesAsync,
  fetchVacationsAsync,
} from "features/human-resouce/actions";
import {
  employees as employeesSelector,
  absences as absencesSelector,
  selectVacations,
} from "features/human-resouce/selectors";

export const HumanResourcesPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchEmployeesAsync.request());
    dispatch(fetchAbsencesAsync.request());
    dispatch(fetchVacationsAsync.request());
  }, [dispatch]);

  const { items: employees } = useSelector(employeesSelector);
  const absences = useSelector(absencesSelector);
  const vacations = useSelector(selectVacations);

  return (
    <HumanResourceCalendar
      employees={employees}
      absences={absences}
      vacations={vacations}
    />
  );
};
