declare module "LocationModels" {
  export type Location = {
    id: string;
    name: string;
    vacationDays: number;
  };

  export type LocationForm = {
    name: string;
    vacationDays: number;
  };

  export type AssignLocationForm = {
    locationId: string;
    startedAt: string;
    extraVacationDays: number;
  };

  export type AssignLocationData = {
    userId: string;
  } & AssignLocationForm;

  export type EditEmployeeLocationForm = {
    locationId: string;
    startedAt: string;
  };

  export type EditEmployeeLocationData = {
    form: EditEmployeeLocationForm;
    employeeLocationId: string;
  };

  export type EmployeeLocation = {
    id: string;
    startedAt: string;
    endedAt: string | null;
    location: {
      id: string;
      name: string;
      vacationDays: number;
    };
    userId: string;
  };

  export type EmployeeLocationsMap = {
    [userId: string]: EmployeeLocation[];
  };

  export type Calendar = {
    id: string;
    name: string;
    location: Location;
  };

  export type Holiday = {
    name: string;
    day: string;
    isWorkingDay: boolean;
  };

  export type AddHolidayForm = {
    day: string;
    name: string;
    isWorkingDay: boolean;
  };

  export type EditHolidayForm = {
    day: string;
    name: string;
    isWorkingDay: boolean;
  };

  export type ChangeCalendar = {
    calendarId: string;
    locationId: string;
    name: string;
    encodedCalendar: string;
  };

  ///////////////////////////////////////////////////////////

  export type LocationResponse = {
    data: {
      locations: Location[];
    };
  };

  export type EmployeeLocationsResponse = {
    data: {
      userLocations: EmployeeLocation[];
    };
  };

  export type CalendarResponse = {
    data: { calendars: Calendar[] };
  };

  export type HolidayResponse = {
    data: { holidays: Holiday[] };
  };

  export type ImportCalendarRequest = {
    locationId: string;
    name: string;
    encodedCalendar: string;
  };

  export type AddHolidayRequest = {
    calendarId: string;
    day: string;
    name: string;
    isWorkingDay: boolean;
  };

  export type EditHolidayRequest = {
    calendarId: string;
    day: string;
    name: string;
    isWorkingDay: boolean;
  };

  export type RemoveHolidayRequest = {
    calendarId: string;
    day: string;
  };
}
