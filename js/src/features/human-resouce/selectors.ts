import { RootState } from "MyTypes";
import { CalendarVacationItem } from "HumanResourceModels";

export const vacations = (state: RootState) => state.humanResource.vacations;

export const vacationItems = (state: RootState) => {
  const items: CalendarVacationItem[] = [];

  state.humanResource.vacations.items.forEach(item => {
    item.vacations.forEach(vacation => {
      items.push({
        id: `${item.id}${vacation.description}`,
        group: item.id,
        title: vacation.description,
        start_time: new Date(vacation.from).getTime(),
        end_time: new Date(vacation.to).getTime(),
      });
    });
  });

  return items;
};

export const vacationGroups = (state: RootState) =>
  state.humanResource.vacations.items.map(item => ({
    id: item.id,
    title: item.username,
    item,
  }));

export const userDetails = (state: RootState) => state.humanResource.details;

export const profile = (state: RootState) => state.humanResource.profile;
