declare module "HumanResourceModels" {
  export type User = {
    id: number;
    username: string;
    avatar: string;
  };

  type UserVacation = {
    from: string;
    to: string;
    description: string;
  };

  export type UserVacations = {
    id: number;
    username: string;
    avatar: string;
    vacations: UserVacation[];
  };

  export type UserContact = {
    type: string;
    value: string;
  };

  export type UserNote = {
    id: number;
    text: string;
    archived: boolean;
    createdAt: string;
  };

  export type UserDetails = {
    id: number;
    username: string;
    email: string;
    avatar: string;
    firstName: string;
    lastName: string;
    birthday: string;
    phone: string;
    description: string;
    position: string;
    skills: string[];
    vacations: UserVacation[];
    contacts: UserContact[];
    notes: UserNote[];
  };

  export type UsersListing = {
    items: User[];
    page: number;
    perPage: number;
    haveMore: boolean;
  };

  export type UsersParams = {
    page?: number;
    perPage?: number;
  };
}
