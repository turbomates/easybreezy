declare module "MyTypes" {
  export type Success<T> = { success: true; data: T };
  export type Failure = { success: false; reason: string };
  export type FormFailure = { success: false; errors: FormError[] };
  export type Services = typeof import("./index").default;

  export type Paging<T> = {
    pageSize: number;
    currentPage: number;
    hasMore: boolean;
    data: T[];
  };

  export type FormError = {
    message: string;
    property: string;
    value: any;
  };

  export type FormErrorMap = {
    [key: string]: FormError;
  };
}
