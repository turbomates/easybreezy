declare module "AccountModules" {
  export type Profile = {
    id: string;
    email: string;
    status: string;
    roles: string[];
  };

  export type ProfileResponse = {
    data: Profile;
  };
}
