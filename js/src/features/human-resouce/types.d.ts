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

  export type UserDetails = {
    id: number;
    username: string;
    avatar: string;
    firstName: string;
    lastName: string;
    phone: string;
    description: string;
    vacations: UserVacation[];
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
