import { Failure, Success } from "MyTypes";
import {
  UsersListing,
  UsersParams,
  UserVacations,
  UserDetails,
} from "HumanResourceModels";
import { usersData, UserJSON } from "./data";

const getUserGeneralInfo = (item: UserJSON) => ({
  id: item.id,
  username: item.username,
  avatar: item.avatar,
});

const getUserDetails = (item: UserJSON) => ({
  id: item.id,
  username: item.username,
  email: item.email,
  avatar: item.avatar,
  firstName: item.firstName,
  lastName: item.lastName,
  birthday: item.birthday,
  phone: item.phone,
  description: item.description,
  position: item.position,
  vacations: item.vacations,
  contacts: item.contacts,
  notes: item.notes,
  skills: item.skills,
});

const getUserVacationInfo = (item: UserJSON) => ({
  id: item.id,
  username: item.username,
  avatar: item.avatar,
  vacations: item.vacations,
});

type UsersResponse = Promise<Success<UsersListing> | Failure>;

export const fetchUsers = (params: UsersParams): UsersResponse => {
  const perPage = params.perPage || 10;
  const page = params.page || 1;

  const start = (page - 1) * perPage;
  const end = start + perPage;

  return Promise.resolve({
    success: true,
    data: {
      items: usersData.slice(start, end).map(getUserGeneralInfo),
      page,
      perPage,
      haveMore: end < usersData.length,
    },
  });
};

type UsersVacationsResponse = Promise<Success<UserVacations[]> | Failure>;

export const fetchVacations = (): UsersVacationsResponse => {
  return Promise.resolve({
    success: true,
    data: usersData.map(getUserVacationInfo),
  });
};

type UserResponse = Promise<Success<UserDetails> | Failure>;

export const fetchUserDetails = (id: string): UserResponse => {
  const user = usersData.find((item) => item.id === parseInt(id));

  const response = user
    ? ({
        success: true,
        data: getUserDetails(user),
      } as Success<UserDetails>)
    : ({
        success: false,
        reason: "User isn't found",
      } as Failure);

  return Promise.resolve(response);
};

export const fetchProfile = (): UserResponse =>
  Promise.resolve({
    success: true,
    data: getUserDetails(usersData[0]),
  });
