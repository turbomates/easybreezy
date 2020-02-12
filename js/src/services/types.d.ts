declare module "MyTypes" {
  export type Success<T> = { success: true, data: T }
  export type Failed = { success: false, reason: string }
  export type Services = typeof import("./index").default;
}
