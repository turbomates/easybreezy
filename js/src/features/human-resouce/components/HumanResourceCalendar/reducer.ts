import addDate from "date-fns/fp/add";

import { calculateCellCount } from "./helpers";

export type State = {
  date: Date;
  daysCount: number;
  scrollDaysCount: number;
};
export type Action =
  | { type: "move"; direction: "back" | "forward" }
  | { type: "setDaysCount"; daysCount: number }
  | { type: "scroll"; daysCount: number }
  | { type: "today" };

export const initial: State = {
  date: new Date(),
  daysCount: calculateCellCount(),
  scrollDaysCount: 7,
};

export function reducer(state: State, action: Action): State {
  switch (action.type) {
    case "move":
      const days =
        action.direction === "back" ? -state.daysCount : state.daysCount;
      return { ...state, date: addDate({ days }, state.date) };
    case "setDaysCount":
      return {
        ...state,
        daysCount: action.daysCount >= 30 ? 30 : action.daysCount,
      };
    case "scroll":
      return {
        ...state,
        date: addDate({ days: action.daysCount }, state.date),
      };
    case "today":
      return {
        ...state,
        date: new Date(),
      };
    default:
      return state;
  }
}
