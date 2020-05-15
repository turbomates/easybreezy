import isWithinInterval from "date-fns/isWithinInterval";
import isPast from "date-fns/isPast";
import parseISO from "date-fns/parseISO";

export const isCurrentInterval = (
  startedAt: string,
  endedAt: string | null,
) => {
  const start = parseISO(startedAt);

  if (!endedAt) return isPast(start);

  const end = parseISO(endedAt);
  const now = new Date();

  return isWithinInterval(now, { start, end });
};
