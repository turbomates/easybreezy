import { RootState } from "MyTypes";
// import { CalendarVacationItem } from "HumanResourceModels";

// export const vacations = (state: RootState) => state.humanResource.vacations;

// export const vacationItems = (state: RootState) => {
//   const items: CalendarVacationItem[] = [];

//   state.humanResource.vacations.items.forEach(item => {
//     item.vacations.forEach(vacation => {
//       items.push({
//         id: `${item.id}${vacation.description}`,
//         group: item.id,
//         title: vacation.description,
//         start_time: new Date(vacation.from).getTime(),
//         end_time: new Date(vacation.to).getTime(),
//       });
//     });
//   });

//   return items;
// };

export const employees = (state: RootState) => state.humanResource.employees;

export const calendarGroups = (state: RootState) =>
  state.humanResource.employees.items.map((item) => ({
    id: item.userId,
    title: item.userId,
    item,
  }));

export const employeeDetails = (state: RootState) =>
  state.humanResource.details;
