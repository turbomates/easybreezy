declare module "AuthModels" {
  export type User = {
    username: string;
    email: string;
  };

  export type SignInData = {
    username: string;
    password: string;
  }
}
