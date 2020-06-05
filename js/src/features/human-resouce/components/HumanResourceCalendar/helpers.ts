import eachDay from "date-fns/fp/eachDayOfInterval";
import getYear from "date-fns/fp/getYear";
import addDate from "date-fns/fp/add";
import formatDate from "date-fns/fp/format";
import parseISO from "date-fns/fp/parseISO";
import isWithinInterval from "date-fns/fp/isWithinInterval";
import isWeekend from "date-fns/fp/isWeekend";
import compose from "lodash/fp/compose";
import groupBy from "lodash/fp/groupBy";
import toPairs from "lodash/fp/toPairs";
import toLower from "lodash/fp/toLower";
import differenceInDays from "date-fns/fp/differenceInDays";

import { Absence, AbsenceReason } from "HumanResourceModels";

export function getDateInterval(date: Date, cells: number) {
  const start = addDate({ days: -cells }, date);
  const end = addDate({ days: cells }, date);
  const title =
    getYear(start) === getYear(end)
      ? `${formatDate("MMM", start)} - ${formatDate("MMM yyyy", end)}`
      : `${formatDate("MMM yyyy", start)} - ${formatDate("MMM yyyy", end)}`;
  return { interval: eachDay({ start, end }), format: "dd", title };
}

export const getMonthsFromInterval = compose(
  toPairs,
  groupBy(formatDate("MMM")),
);

export function generateCellInfo(date: Date, absences: Absence[] = []) {
  for (const absence of absences) {
    const interval = {
      start: parseISO(absence.startedAt),
      end: parseISO(absence.endedAt),
    };
    if (isWithinInterval(interval, date)) {
      const reason = toLower(absence.reason);
      const approved = absence.isApproved ? "" : "not-approved";
      const title = absence.isApproved
        ? absence.comment
        : `${absence.comment} (not approved)`;
      return { className: `${reason} ${approved}`, title };
    }
  }
  if (isWeekend(date)) {
    return { className: "weekend", title: formatDate("eeee", date) };
  }
  return { className: "", title: "" };
}

export function getAbsencesDays(absences: Absence[], reason: AbsenceReason) {
  return absences.reduce((acc, absence) => {
    if (absence.reason === reason) {
      acc =
        acc +
        differenceInDays(
          new Date(absence.startedAt),
          new Date(absence.endedAt),
        );
    }

    return acc;
  }, 1);
}

export function getCapitalLetter(value: string | null) {
  return value ? value.charAt(0) : "";
}

export function getCountCell() {
  const screenWidth = window.innerWidth;
  const asideMenuWidth = 200;
  const tableUserWidth = 200;
  const tableInfoWidth = 140;
  const cellWidth = 30;

  return Math.ceil(
    ((screenWidth - asideMenuWidth - tableInfoWidth - tableUserWidth) * 0.45) /
      cellWidth,
  );
}
