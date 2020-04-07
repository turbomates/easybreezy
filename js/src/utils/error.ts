import { FormError, FormErrorMap } from "MyTypes";

export const normalizeErrors = (errors: FormError[]): FormErrorMap =>
  errors.reduce((result, error) => {
    result[error.property] = error;
    return result;
  }, {} as FormErrorMap);
