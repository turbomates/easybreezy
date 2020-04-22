declare module "LocationModels" {
  export type Location = {
    id: string;
    name: string;
    vacationDays: number;
  };

  export type LocationResponse = {
    data: {
      locations: Location[];
    };
  };

  export type LocationForm = {
    name: string;
    vacationDays: number;
  };

  export type AssignLocationForm = {
    locationId: string;
    startedAt: string;
    endedAt: string;
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
    endedAt: string;
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

  export type EmployeeLocationsResponse = {
    data: {
      userLocations: EmployeeLocationsMap;
    };
  };
}
