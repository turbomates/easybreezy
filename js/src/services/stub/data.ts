export interface UserJSON {
  id: number;
  username: string;
  avatar: string;
  firstName: string;
  lastName: string;
  phone: string;
  description: string;
  vacations: {
    description: string;
    from: string;
    to: string;
  }[];
}

export const usersData: UserJSON[] = [
  {
    id: 1,
    username: "testuser1",
    avatar: "https://i.pravatar.cc/150?img=1",
    firstName: "Ivan",
    lastName: "Andreev",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      {
        description: "Two weeks on the beach",
        from: "2019-11-01",
        to: "2019-11-15",
      },
      { description: "Test vacation 2", from: "2020-01-01", to: "2020-01-21" },
    ],
  },
  {
    id: 2,
    username: "testuser2",
    avatar: "https://i.pravatar.cc/150?img=2",
    firstName: "Andrey",
    lastName: "Ivanov",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2019-09-01", to: "2019-10-20" },
      { description: "Test vacation 2", from: "2020-02-01", to: "2020-02-11" },
    ],
  },
  {
    id: 3,
    username: "testuser3",
    avatar: "https://i.pravatar.cc/150?img=3",
    firstName: "Yury",
    lastName: "Smirnov",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Relaxing in Bali", from: "2019-10-12", to: "2020-01-20" },
    ],
  },
  {
    id: 4,
    username: "testuser4",
    avatar: "https://i.pravatar.cc/150?img=4",
    firstName: "Nikita",
    lastName: "Jukov",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2019-09-15", to: "2019-09-25" },
      { description: "Test vacation 2", from: "2020-03-01", to: "2020-03-11" },
    ],
  },
  {
    id: 5,
    username: "testuser5",
    avatar: "https://i.pravatar.cc/150?img=5",
    firstName: "Alex",
    lastName: "Antonov",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2019-09-15", to: "2019-09-25" },
      { description: "Test vacation 2", from: "2020-03-01", to: "2020-03-11" },
    ],
  },
  {
    id: 6,
    username: "testuser6",
    avatar: "https://i.pravatar.cc/150?img=6",
    firstName: "Julia",
    lastName: "Smith",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2019-12-15", to: "2019-12-25" },
      { description: "Test vacation 2", from: "2020-01-20", to: "2020-01-25" },
    ],
  },
  {
    id: 7,
    username: "testuser7",
    avatar: "https://i.pravatar.cc/150?img=7",
    firstName: "Marcus",
    lastName: "Brown",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2020-04-01", to: "2020-04-16" },
    ],
  },
  {
    id: 8,
    username: "testuser8",
    avatar: "https://i.pravatar.cc/150?img=8",
    firstName: "Lidia",
    lastName: "White",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2020-02-20", to: "2019-02-27" },
    ],
  },
  {
    id: 9,
    username: "testuser9",
    avatar: "https://i.pravatar.cc/150?img=9",
    firstName: "Antony",
    lastName: "Roggers",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [],
  },
  {
    id: 10,
    username: "testuser10",
    avatar: "https://i.pravatar.cc/150?img=10",
    firstName: "Mike",
    lastName: "Gross",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2019-08-20", to: "2019-09-05" },
    ],
  },
  {
    id: 11,
    username: "testuser11",
    avatar: "https://i.pravatar.cc/150?img=11",
    firstName: "Alisia",
    lastName: "North",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    vacations: [
      { description: "Test vacation 1", from: "2020-05-05", to: "2019-05-15" },
    ],
  },
];
