declare module "AuthModels" {
  export type User = {
    sub: string;
    aud: "easybreezy";
    once: boolean;
    rnd: number;
    exp: number;
    iat: number;
  };

  export type SignInData = {
    email: string;
    password: string;
  };

  export type RulesMap = {
    [rule: string]: string[];
  };
}
