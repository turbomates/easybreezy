declare module "AccountModules" {
  export type Profile = {
    id: string;
    email: string;
    status: string;
    activities: string[];
  };

  export type ProfileResponse = {
    data: Profile;
  };
}
