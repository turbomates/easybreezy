import moment from "moment";

export const isTodayInRange = (start: string, end: string) => {
  const from = moment(start);
  const to = moment(end);
  const today = moment();

  return from.isBefore(today) && to.isAfter(today);
};
