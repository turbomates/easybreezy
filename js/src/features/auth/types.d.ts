declare module "AuthModels" {
  export type User = {
    username: string;
    email: string;
  };

  export type SignInData = {
    email: string;
    password: string;
  };
}
