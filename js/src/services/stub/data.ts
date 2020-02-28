// val userId: UUID,
// val firstName: String?,
// val lastName: String?,

// username
// avatar

// val birthday: String?,
// val bio: String?,

// val skills: List<String>?,
// var positions: List<Position> = listOf(),
// var contacts: List<Contact> = listOf()

// var notes: List<Note> = listOf(),
// var salaries: List<Salary> = listOf(),

export interface Position {
  id: string;
  title: string;
  since: string;
  till: string;
}

export interface UserJSON {
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
  vacations: {
    description: string;
    from: string;
    to: string;
  }[];
  contacts: {
    type: string;
    value: string;
  }[];
  notes: {
    id: number;
    text: string;
    archived: boolean;
    createdAt: string;
  }[];
  skills: string[];
}

export const usersData: UserJSON[] = [
  {
    id: 1,
    username: "testuser1",
    email: "testuser1@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=1",
    firstName: "Ivan",
    lastName: "Andreev",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      {
        description: "Two weeks on the beach",
        from: "2019-11-01",
        to: "2019-11-15",
      },
      { description: "Test vacation 2", from: "2020-01-01", to: "2020-01-21" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "email",
        value: "testuser1@gmail.com",
      },
      {
        type: "skype",
        value: "test",
      },
      {
        type: "playstationID",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 2,
    username: "testuser2",
    email: "testuser2@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=2",
    firstName: "Andrey",
    lastName: "Ivanov",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2019-09-01", to: "2019-10-20" },
      { description: "Test vacation 2", from: "2020-02-01", to: "2020-02-11" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 3,
    username: "testuser3",
    email: "testuser3@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=3",
    firstName: "Yury",
    lastName: "Smirnov",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Relaxing in Bali", from: "2019-10-12", to: "2020-01-20" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 4,
    username: "testuser4",
    email: "testuser4@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=4",
    firstName: "Nikita",
    lastName: "Jukov",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2019-09-15", to: "2019-09-25" },
      { description: "Test vacation 2", from: "2020-03-01", to: "2020-03-11" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 5,
    username: "testuser5",
    email: "testuser5@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=5",
    firstName: "Alex",
    lastName: "Antonov",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2019-09-15", to: "2019-09-25" },
      { description: "Test vacation 2", from: "2020-03-01", to: "2020-03-11" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 6,
    username: "testuser6",
    email: "testuser6@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=6",
    firstName: "Julia",
    lastName: "Smith",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2019-12-15", to: "2019-12-25" },
      { description: "Test vacation 2", from: "2020-01-20", to: "2020-01-25" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 7,
    username: "testuser7",
    email: "testuser7@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=7",
    firstName: "Marcus",
    lastName: "Brown",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2020-04-01", to: "2020-04-16" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 8,
    username: "testuser8",
    email: "testuser8@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=8",
    firstName: "Lidia",
    lastName: "White",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2020-02-20", to: "2019-02-27" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 9,
    username: "testuser9",
    email: "testuser9@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=9",
    firstName: "Antony",
    lastName: "Roggers",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 10,
    username: "testuser10",
    email: "testuser10@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=10",
    firstName: "Mike",
    lastName: "Gross",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2019-08-20", to: "2019-09-05" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
  {
    id: 11,
    username: "testuser11",
    email: "testuser11@gmail.com",
    avatar: "https://i.pravatar.cc/150?img=11",
    firstName: "Alisia",
    lastName: "North",
    birthday: "1990-01-01",
    phone: "+375678912345",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    position: "developer",
    skills: ["typescript", "javascript", "React", "Redux", "Mobx", "Reselect"],
    vacations: [
      { description: "Test vacation 1", from: "2020-05-05", to: "2019-05-15" },
    ],
    contacts: [
      {
        type: "phone",
        value: "+375212345678",
      },
      {
        type: "skype",
        value: "test",
      },
    ],
    notes: [
      {
        id: 1,
        text: "Cool guy. We spend a lot of time having fun.",
        archived: false,
        createdAt: "2020-01-10",
      },
      {
        id: 2,
        text:
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        archived: false,
        createdAt: "2020-01-12",
      },
    ],
  },
];
